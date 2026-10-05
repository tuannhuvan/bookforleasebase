package com.library.management.dto.response;

import java.math.BigDecimal;

public class BorrowingDetailDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private Integer quantity;
    private Integer returnedQuantity;
    private BigDecimal fineAmount;

    public BorrowingDetailDTO() {
    }

    public BorrowingDetailDTO(Long id, Long bookId, String bookTitle, String bookIsbn, Integer quantity, Integer returnedQuantity, BigDecimal fineAmount) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookIsbn = bookIsbn;
        this.quantity = quantity;
        this.returnedQuantity = returnedQuantity;
        this.fineAmount = fineAmount;
    }

    public static BorrowingDetailDTOBuilder builder() {
        return new BorrowingDetailDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getReturnedQuantity() { return returnedQuantity; }
    public void setReturnedQuantity(Integer returnedQuantity) { this.returnedQuantity = returnedQuantity; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }

    public static class BorrowingDetailDTOBuilder {
        private Long id;
        private Long bookId;
        private String bookTitle;
        private String bookIsbn;
        private Integer quantity;
        private Integer returnedQuantity;
        private BigDecimal fineAmount;

        public BorrowingDetailDTOBuilder id(Long id) { this.id = id; return this; }
        public BorrowingDetailDTOBuilder bookId(Long bookId) { this.bookId = bookId; return this; }
        public BorrowingDetailDTOBuilder bookTitle(String bookTitle) { this.bookTitle = bookTitle; return this; }
        public BorrowingDetailDTOBuilder bookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; return this; }
        public BorrowingDetailDTOBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public BorrowingDetailDTOBuilder returnedQuantity(Integer returnedQuantity) { this.returnedQuantity = returnedQuantity; return this; }
        public BorrowingDetailDTOBuilder fineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; return this; }

        public BorrowingDetailDTO build() {
            return new BorrowingDetailDTO(id, bookId, bookTitle, bookIsbn, quantity, returnedQuantity, fineAmount);
        }
    }
}
