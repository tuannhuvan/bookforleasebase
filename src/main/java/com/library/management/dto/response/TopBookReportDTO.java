package com.library.management.dto.response;

public class TopBookReportDTO {
    private Long bookId;
    private String title;
    private String isbn;
    private Long totalBorrowed;

    public TopBookReportDTO() {
    }

    public TopBookReportDTO(Long bookId, String title, String isbn, Long totalBorrowed) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.totalBorrowed = totalBorrowed;
    }

    public static TopBookReportDTOBuilder builder() {
        return new TopBookReportDTOBuilder();
    }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Long getTotalBorrowed() { return totalBorrowed; }
    public void setTotalBorrowed(Long totalBorrowed) { this.totalBorrowed = totalBorrowed; }

    public static class TopBookReportDTOBuilder {
        private Long bookId;
        private String title;
        private String isbn;
        private Long totalBorrowed;

        public TopBookReportDTOBuilder bookId(Long bookId) { this.bookId = bookId; return this; }
        public TopBookReportDTOBuilder title(String title) { this.title = title; return this; }
        public TopBookReportDTOBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public TopBookReportDTOBuilder totalBorrowed(Long totalBorrowed) { this.totalBorrowed = totalBorrowed; return this; }

        public TopBookReportDTO build() {
            return new TopBookReportDTO(bookId, title, isbn, totalBorrowed);
        }
    }
}
