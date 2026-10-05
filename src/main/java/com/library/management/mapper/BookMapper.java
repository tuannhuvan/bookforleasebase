package com.library.management.mapper;

import com.library.management.dto.request.BookForm;
import com.library.management.dto.response.BookDTO;
import com.library.management.entity.Book;

public class BookMapper {

    public static BookDTO toDTO(Book book) {
        if (book == null) return null;
        return BookDTO.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalQuantity(book.getTotalQuantity())
                .availableQuantity(book.getAvailableQuantity())
                .status(book.getStatus())
                .categoryId(book.getCategory() != null ? book.getCategory().getId() : null)
                .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
                .version(book.getVersion())
                .build();
    }

    public static BookForm toForm(Book book) {
        if (book == null) return null;
        return BookForm.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalQuantity(book.getTotalQuantity())
                .categoryId(book.getCategory() != null ? book.getCategory().getId() : null)
                .status(book.getStatus())
                .build();
    }
}
