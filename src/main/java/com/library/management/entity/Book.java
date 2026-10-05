package com.library.management.entity;

import com.library.management.enums.BookStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookStatus status;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Book() {
    }

    public Book(String isbn, String title, String author, Integer totalQuantity, Integer availableQuantity, BookStatus status, Long version, Category category) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.status = status;
        this.version = version;
        this.category = category;
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

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
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public static class BookBuilder {
        private String isbn;
        private String title;
        private String author;
        private Integer totalQuantity;
        private Integer availableQuantity;
        private BookStatus status;
        private Long version;
        private Category category;

        public BookBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookBuilder title(String title) { this.title = title; return this; }
        public BookBuilder author(String author) { this.author = author; return this; }
        public BookBuilder totalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; return this; }
        public BookBuilder availableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; return this; }
        public BookBuilder status(BookStatus status) { this.status = status; return this; }
        public BookBuilder version(Long version) { this.version = version; return this; }
        public BookBuilder category(Category category) { this.category = category; return this; }

        public Book build() {
            return new Book(isbn, title, author, totalQuantity, availableQuantity, status, version, category);
        }
    }
}
