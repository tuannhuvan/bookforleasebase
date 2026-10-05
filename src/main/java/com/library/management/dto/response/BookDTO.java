package com.library.management.dto.response;

import com.library.management.enums.BookStatus;

public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private BookStatus status;
    private Long categoryId;
    private String categoryName;
    private Long version;

    public BookDTO() {
    }

    public BookDTO(Long id, String isbn, String title, String author, Integer totalQuantity, Integer availableQuantity, BookStatus status, Long categoryId, String categoryName, Long version) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.version = version;
    }

    public static BookDTOBuilder builder() {
        return new BookDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public static class BookDTOBuilder {
        private Long id;
        private String isbn;
        private String title;
        private String author;
        private Integer totalQuantity;
        private Integer availableQuantity;
        private BookStatus status;
        private Long categoryId;
        private String categoryName;
        private Long version;

        public BookDTOBuilder id(Long id) { this.id = id; return this; }
        public BookDTOBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookDTOBuilder title(String title) { this.title = title; return this; }
        public BookDTOBuilder author(String author) { this.author = author; return this; }
        public BookDTOBuilder totalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; return this; }
        public BookDTOBuilder availableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; return this; }
        public BookDTOBuilder status(BookStatus status) { this.status = status; return this; }
        public BookDTOBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public BookDTOBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public BookDTOBuilder version(Long version) { this.version = version; return this; }

        public BookDTO build() {
            return new BookDTO(id, isbn, title, author, totalQuantity, availableQuantity, status, categoryId, categoryName, version);
        }
    }
}
