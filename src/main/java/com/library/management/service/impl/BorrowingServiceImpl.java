package com.library.management.service.impl;

import com.library.management.dto.request.BorrowingRequest;
import com.library.management.dto.request.ReturnRequest;
import com.library.management.dto.response.BorrowingDTO;
import com.library.management.entity.Book;
import com.library.management.entity.Borrowing;
import com.library.management.entity.BorrowingDetail;
import com.library.management.entity.Member;
import com.library.management.enums.BookStatus;
import com.library.management.enums.BorrowingStatus;
import com.library.management.enums.MemberStatus;
import com.library.management.exception.BusinessRuleException;
import com.library.management.exception.OutOfStockException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.mapper.BorrowingMapper;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowingRepository;
import com.library.management.repository.MemberRepository;
import com.library.management.service.BorrowingService;
import com.library.management.specification.BorrowingSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class BorrowingServiceImpl implements BorrowingService {

    private static final long DAILY_FINE_PER_BOOK = 5000L;
    private static final int DEFAULT_BORROW_DAYS = 14;

    private final BorrowingRepository borrowingRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public BorrowingServiceImpl(BorrowingRepository borrowingRepository, MemberRepository memberRepository, BookRepository bookRepository) {
        this.borrowingRepository = borrowingRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<BorrowingDTO> findAll(String keyword, BorrowingStatus status, Pageable pageable) {
        var spec = BorrowingSpecification.filter(keyword, status);
        return borrowingRepository.findAll(spec, pageable).map(BorrowingMapper::toDTO);
    }

    @Override
    public BorrowingDTO findById(Long id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu mượn với ID: " + id));
        return BorrowingMapper.toDTO(borrowing);
    }

    @Override
    @Transactional
    public BorrowingDTO createBorrowing(BorrowingRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành viên với ID: " + request.getMemberId()));

        if (member.getStatus() == MemberStatus.BLOCKED) {
            throw new BusinessRuleException("Thành viên '" + member.getFullName() + "' đang ở trạng thái Bị khóa (BLOCKED), không được phép lập phiếu mượn!");
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(DEFAULT_BORROW_DAYS);

        Borrowing borrowing = Borrowing.builder()
                .member(member)
                .borrowDate(today)
                .dueDate(dueDate)
                .status(BorrowingStatus.BORROWED)
                .totalFine(BigDecimal.ZERO)
                .details(new ArrayList<>())
                .build();

        for (BorrowingRequest.BorrowingItemRequest item : request.getItems()) {
            Book book = bookRepository.findByIdForUpdate(item.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + item.getBookId()));

            if (book.getStatus() != BookStatus.AVAILABLE) {
                throw new BusinessRuleException("Cuốn sách '" + book.getTitle() + "' hiện không ở trạng thái sẵn sàng để mượn!");
            }

            if (book.getAvailableQuantity() < item.getQuantity()) {
                throw new OutOfStockException("Sách '" + book.getTitle() + "' chỉ còn khả dụng "
                        + book.getAvailableQuantity() + " cuốn, không đủ để mượn " + item.getQuantity() + " cuốn!");
            }

            // Deduct available quantity
            book.setAvailableQuantity(book.getAvailableQuantity() - item.getQuantity());
            bookRepository.save(book);

            BorrowingDetail detail = BorrowingDetail.builder()
                    .borrowing(borrowing)
                    .book(book)
                    .quantity(item.getQuantity())
                    .returnedQuantity(0)
                    .fineAmount(BigDecimal.ZERO)
                    .build();

            borrowing.getDetails().add(detail);
        }

        Borrowing saved = borrowingRepository.save(borrowing);
        return BorrowingMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public BorrowingDTO processReturn(Long borrowingId, ReturnRequest request) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu mượn với ID: " + borrowingId));

        if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
            throw new BusinessRuleException("Phiếu mượn này đã được trả hoàn tất trước đó!");
        }

        LocalDate today = LocalDate.now();
        long overdueDays = 0;
        if (today.isAfter(borrowing.getDueDate())) {
            overdueDays = ChronoUnit.DAYS.between(borrowing.getDueDate(), today);
        }

        BigDecimal additionalTotalFine = BigDecimal.ZERO;

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (ReturnRequest.ReturnItemRequest itemReq : request.getItems()) {
                if (itemReq.getReturnQuantity() == null || itemReq.getReturnQuantity() <= 0) {
                    continue;
                }

                BorrowingDetail detail = borrowing.getDetails().stream()
                        .filter(d -> d.getId().equals(itemReq.getDetailId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dòng chi tiết phiếu mượn ID: " + itemReq.getDetailId()));

                int remainingToReturn = detail.getQuantity() - detail.getReturnedQuantity();
                if (itemReq.getReturnQuantity() > remainingToReturn) {
                    throw new BusinessRuleException("Số lượng trả (" + itemReq.getReturnQuantity()
                            + ") vượt quá số lượng còn phải trả (" + remainingToReturn + ") cho sách '"
                            + detail.getBook().getTitle() + "'!");
                }

                // Update detail returned quantity
                detail.setReturnedQuantity(detail.getReturnedQuantity() + itemReq.getReturnQuantity());

                // Return quantity back to Book stock
                Book book = bookRepository.findByIdForUpdate(detail.getBook().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + detail.getBook().getId()));
                book.setAvailableQuantity(book.getAvailableQuantity() + itemReq.getReturnQuantity());
                bookRepository.save(book);

                // Fine calculation if overdue
                if (overdueDays > 0) {
                    BigDecimal fineForThisReturn = BigDecimal.valueOf(overdueDays * DAILY_FINE_PER_BOOK * itemReq.getReturnQuantity());
                    detail.setFineAmount(detail.getFineAmount().add(fineForThisReturn));
                    additionalTotalFine = additionalTotalFine.add(fineForThisReturn);
                }
            }
        }

        // Recalculate borrowing total fine
        BigDecimal totalFineSum = borrowing.getDetails().stream()
                .map(BorrowingDetail::getFineAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        borrowing.setTotalFine(totalFineSum);

        // Check if all details fully returned
        boolean allReturned = borrowing.getDetails().stream()
                .allMatch(d -> d.getReturnedQuantity().equals(d.getQuantity()));

        if (allReturned) {
            borrowing.setStatus(BorrowingStatus.RETURNED);
            borrowing.setReturnedDate(today);
        } else {
            borrowing.setStatus(BorrowingStatus.PARTIAL_RETURNED);
        }

        Borrowing updated = borrowingRepository.save(borrowing);
        return BorrowingMapper.toDTO(updated);
    }
}
