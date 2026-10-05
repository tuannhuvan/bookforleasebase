package com.library.management.service.impl;

import com.library.management.dto.request.BookForm;
import com.library.management.dto.response.BookDTO;
import com.library.management.entity.Book;
import com.library.management.entity.Category;
import com.library.management.enums.BookStatus;
import com.library.management.exception.BusinessRuleException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.mapper.BookMapper;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowingRepository;
import com.library.management.repository.CategoryRepository;
import com.library.management.service.BookService;
import com.library.management.specification.BookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BorrowingRepository borrowingRepository;

    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, BorrowingRepository borrowingRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.borrowingRepository = borrowingRepository;
    }

    @Override
    public Page<BookDTO> findAll(String keyword, Long categoryId, BookStatus status, Pageable pageable) {
        var spec = BookSpecification.filter(keyword, categoryId, status);
        return bookRepository.findAll(spec, pageable).map(BookMapper::toDTO);
    }

    @Override
    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + id));
        return BookMapper.toDTO(book);
    }

    @Override
    @Transactional
    public BookDTO createBook(BookForm form) {
        if (bookRepository.existsByIsbn(form.getIsbn().trim())) {
            throw new BusinessRuleException("Mã ISBN '" + form.getIsbn() + "' đã tồn tại!");
        }

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thể loại với ID: " + form.getCategoryId()));

        Book book = Book.builder()
                .isbn(form.getIsbn().trim())
                .title(form.getTitle().trim())
                .author(form.getAuthor().trim())
                .totalQuantity(form.getTotalQuantity())
                .availableQuantity(form.getTotalQuantity())
                .status(form.getStatus() != null ? form.getStatus() : BookStatus.AVAILABLE)
                .category(category)
                .build();

        Book saved = bookRepository.save(book);
        return BookMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookForm form) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + id));

        if (bookRepository.existsByIsbnAndIdNot(form.getIsbn().trim(), id)) {
            throw new BusinessRuleException("Mã ISBN '" + form.getIsbn() + "' đã tồn tại ở cuốn sách khác!");
        }

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thể loại với ID: " + form.getCategoryId()));

        int oldTotal = book.getTotalQuantity();
        int oldAvailable = book.getAvailableQuantity();
        int currentlyBorrowed = oldTotal - oldAvailable;
        int newTotal = form.getTotalQuantity();

        if (newTotal < currentlyBorrowed) {
            throw new BusinessRuleException("Không thể giảm tổng số lượng xuống " + newTotal +
                    " vì hiện tại đang có " + currentlyBorrowed + " cuốn đang được mượn!");
        }

        int newAvailable = newTotal - currentlyBorrowed;

        book.setIsbn(form.getIsbn().trim());
        book.setTitle(form.getTitle().trim());
        book.setAuthor(form.getAuthor().trim());
        book.setTotalQuantity(newTotal);
        book.setAvailableQuantity(newAvailable);
        book.setCategory(category);
        if (form.getStatus() != null) {
            book.setStatus(form.getStatus());
        }

        Book updated = bookRepository.save(book);
        return BookMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + id));

        if (borrowingRepository.isBookCurrentlyBorrowed(id)) {
            throw new BusinessRuleException("Không thể xóa sách '" + book.getTitle() + "' vì đang có lượt mượn chưa trả xong!");
        }

        book.setStatus(BookStatus.DELETED);
        bookRepository.save(book);
    }
}
