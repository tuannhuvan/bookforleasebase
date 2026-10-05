package com.library.management.service;

import com.library.management.dto.request.BookForm;
import com.library.management.dto.response.BookDTO;
import com.library.management.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookDTO> findAll(String keyword, Long categoryId, BookStatus status, Pageable pageable);
    BookDTO findById(Long id);
    BookDTO createBook(BookForm form);
    BookDTO updateBook(Long id, BookForm form);
    void deleteBook(Long id);
}
