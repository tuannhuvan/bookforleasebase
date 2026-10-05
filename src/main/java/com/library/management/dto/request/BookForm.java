package com.library.management.dto.request;

import com.library.management.enums.BookStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookForm {

    private Long id;

    @NotBlank(message = "Mã ISBN không được để trống")
    private String isbn;

    @NotBlank(message = "Tên sách không được để trống")
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    private String author;

    @NotNull(message = "Tổng số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer totalQuantity;

    @NotNull(message = "Vui lòng chọn thể loại")
    private Long categoryId;

    private BookStatus status = BookStatus.AVAILABLE;

    public BookForm() {
    }

    public BookForm(Long id, String isbn, String title, String author, Integer totalQuantity, Long categoryId, BookStatus status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalQuantity = totalQuantity;
        this.categoryId = categoryId;
        this.status = status != null ? status : BookStatus.AVAILABLE;
    }

    public static BookFormBuilder builder() {
        return new BookFormBuilder();
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
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    public static class BookFormBuilder {
        private Long id;
        private String isbn;
        private String title;
        private String author;
        private Integer totalQuantity;
        private Long categoryId;
        private BookStatus status = BookStatus.AVAILABLE;

        public BookFormBuilder id(Long id) { this.id = id; return this; }
        public BookFormBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookFormBuilder title(String title) { this.title = title; return this; }
        public BookFormBuilder author(String author) { this.author = author; return this; }
        public BookFormBuilder totalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; return this; }
        public BookFormBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public BookFormBuilder status(BookStatus status) { this.status = status; return this; }

        public BookForm build() {
            return new BookForm(id, isbn, title, author, totalQuantity, categoryId, status);
        }
    }
}
