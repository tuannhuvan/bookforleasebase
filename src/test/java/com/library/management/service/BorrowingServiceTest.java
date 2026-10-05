package com.library.management.service;

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
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowingRepository;
import com.library.management.repository.MemberRepository;
import com.library.management.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    private Member activeMember;
    private Member blockedMember;
    private Book availableBook;

    @BeforeEach
    void setUp() {
        activeMember = Member.builder()
                .fullName("Nguyen Van A")
                .email("a.nguyen@example.com")
                .status(MemberStatus.ACTIVE)
                .build();
        activeMember.setId(1L);

        blockedMember = Member.builder()
                .fullName("Tran Van B")
                .email("b.tran@example.com")
                .status(MemberStatus.BLOCKED)
                .build();
        blockedMember.setId(2L);

        availableBook = Book.builder()
                .isbn("978-0134685991")
                .title("Clean Code")
                .author("Robert C. Martin")
                .totalQuantity(5)
                .availableQuantity(5)
                .status(BookStatus.AVAILABLE)
                .build();
        availableBook.setId(10L);
    }

    @Test
    @DisplayName("Lập phiếu mượn thành công - Trừ tồn kho & Thiết lập hạn trả 14 ngày")
    void createBorrowing_Success() {
        BorrowingRequest request = BorrowingRequest.builder()
                .memberId(1L)
                .items(List.of(
                        BorrowingRequest.BorrowingItemRequest.builder()
                                .bookId(10L)
                                .quantity(2)
                                .build()
                ))
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(activeMember));
        when(bookRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(availableBook));

        Borrowing savedBorrowing = Borrowing.builder()
                .member(activeMember)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BorrowingStatus.BORROWED)
                .totalFine(BigDecimal.ZERO)
                .details(new ArrayList<>())
                .build();
        savedBorrowing.setId(100L);

        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(savedBorrowing);

        BorrowingDTO result = borrowingService.createBorrowing(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(3, availableBook.getAvailableQuantity()); // 5 - 2 = 3
        verify(bookRepository, times(1)).save(availableBook);
        verify(borrowingRepository, times(1)).save(any(Borrowing.class));
    }

    @Test
    @DisplayName("Lập phiếu mượn thất bại khi Thành viên bị khóa (BLOCKED)")
    void createBorrowing_BlockedMember_ThrowsException() {
        BorrowingRequest request = BorrowingRequest.builder()
                .memberId(2L)
                .items(List.of(
                        BorrowingRequest.BorrowingItemRequest.builder()
                                .bookId(10L)
                                .quantity(1)
                                .build()
                ))
                .build();

        when(memberRepository.findById(2L)).thenReturn(Optional.of(blockedMember));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                borrowingService.createBorrowing(request)
        );

        assertTrue(exception.getMessage().contains("BLOCKED"));
        verify(borrowingRepository, never()).save(any(Borrowing.class));
    }

    @Test
    @DisplayName("Lập phiếu mượn thất bại khi Số lượng tồn kho không đủ")
    void createBorrowing_OutOfStock_ThrowsException() {
        availableBook.setAvailableQuantity(1); // Only 1 available

        BorrowingRequest request = BorrowingRequest.builder()
                .memberId(1L)
                .items(List.of(
                        BorrowingRequest.BorrowingItemRequest.builder()
                                .bookId(10L)
                                .quantity(3) // Requesting 3
                                .build()
                ))
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(activeMember));
        when(bookRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(availableBook));

        OutOfStockException exception = assertThrows(OutOfStockException.class, () ->
                borrowingService.createBorrowing(request)
        );

        assertTrue(exception.getMessage().contains("chỉ còn khả dụng 1 cuốn"));
        verify(borrowingRepository, never()).save(any(Borrowing.class));
    }

    @Test
    @DisplayName("Trả sách thành công - Hoàn lại tồn kho & Cập nhật trạng thái RETURNED")
    void processReturn_Success() {
        Borrowing borrowing = Borrowing.builder()
                .member(activeMember)
                .borrowDate(LocalDate.now().minusDays(10))
                .dueDate(LocalDate.now().plusDays(4)) // Not overdue
                .status(BorrowingStatus.BORROWED)
                .totalFine(BigDecimal.ZERO)
                .details(new ArrayList<>())
                .build();
        borrowing.setId(100L);

        availableBook.setAvailableQuantity(3);

        BorrowingDetail detail = BorrowingDetail.builder()
                .borrowing(borrowing)
                .book(availableBook)
                .quantity(2)
                .returnedQuantity(0)
                .fineAmount(BigDecimal.ZERO)
                .build();
        detail.setId(50L);
        borrowing.getDetails().add(detail);

        ReturnRequest request = ReturnRequest.builder()
                .borrowingId(100L)
                .items(List.of(
                        ReturnRequest.ReturnItemRequest.builder()
                                .detailId(50L)
                                .returnQuantity(2)
                                .build()
                ))
                .build();

        when(borrowingRepository.findById(100L)).thenReturn(Optional.of(borrowing));
        when(bookRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(availableBook));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);

        BorrowingDTO result = borrowingService.processReturn(100L, request);

        assertNotNull(result);
        assertEquals(BorrowingStatus.RETURNED, borrowing.getStatus());
        assertEquals(5, availableBook.getAvailableQuantity()); // 3 + 2 = 5
        verify(bookRepository, times(1)).save(availableBook);
    }
}
