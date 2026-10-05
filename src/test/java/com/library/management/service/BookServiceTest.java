package com.library.management.service;

import com.library.management.dto.request.BookForm;
import com.library.management.dto.response.BookDTO;
import com.library.management.entity.Book;
import com.library.management.entity.Category;
import com.library.management.enums.BookStatus;
import com.library.management.exception.BusinessRuleException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowingRepository;
import com.library.management.repository.CategoryRepository;
import com.library.management.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Category category;
    private Book book;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Lap Trinh")
                .active(true)
                .build();
        category.setId(1L);

        book = Book.builder()
                .isbn("978-0134685991")
                .title("Effective Java")
                .author("Joshua Bloch")
                .totalQuantity(10)
                .availableQuantity(10)
                .status(BookStatus.AVAILABLE)
                .category(category)
                .build();
        book.setId(5L);
    }

    @Test
    @DisplayName("Tạo mới sách thành công với ISBN hợp lệ")
    void createBook_Success() {
        BookForm form = BookForm.builder()
                .isbn("978-0134685991")
                .title("Effective Java")
                .author("Joshua Bloch")
                .totalQuantity(10)
                .categoryId(1L)
                .status(BookStatus.AVAILABLE)
                .build();

        when(bookRepository.existsByIsbn("978-0134685991")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.createBook(form);

        assertNotNull(result);
        assertEquals("Effective Java", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Tạo mới sách thất bại khi ISBN đã tồn tại")
    void createBook_DuplicateIsbn_ThrowsException() {
        BookForm form = BookForm.builder()
                .isbn("978-0134685991")
                .title("Duplicate Title")
                .author("Author")
                .totalQuantity(5)
                .categoryId(1L)
                .build();

        when(bookRepository.existsByIsbn("978-0134685991")).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                bookService.createBook(form)
        );

        assertTrue(exception.getMessage().contains("đã tồn tại"));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Xóa sách thất bại khi sách đang được mượn chưa trả")
    void deleteBook_CurrentlyBorrowed_ThrowsException() {
        when(bookRepository.findById(5L)).thenReturn(Optional.of(book));
        when(borrowingRepository.isBookCurrentlyBorrowed(5L)).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                bookService.deleteBook(5L)
        );

        assertTrue(exception.getMessage().contains("đang có lượt mượn chưa trả xong"));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Xóa mềm sách thành công khi không có ai mượn")
    void deleteBook_Success() {
        when(bookRepository.findById(5L)).thenReturn(Optional.of(book));
        when(borrowingRepository.isBookCurrentlyBorrowed(5L)).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.deleteBook(5L);

        assertEquals(BookStatus.DELETED, book.getStatus());
        verify(bookRepository, times(1)).save(book);
    }
}
