package com.library.management.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "borrowing_details")
public class BorrowingDetail extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowing_id", nullable = false)
    private Borrowing borrowing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "returned_quantity")
    private Integer returnedQuantity = 0;

    @Column(name = "fine_amount", precision = 12, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    public BorrowingDetail() {
    }

    public BorrowingDetail(Borrowing borrowing, Book book, Integer quantity, Integer returnedQuantity, BigDecimal fineAmount) {
        this.borrowing = borrowing;
        this.book = book;
        this.quantity = quantity;
        this.returnedQuantity = returnedQuantity != null ? returnedQuantity : 0;
        this.fineAmount = fineAmount != null ? fineAmount : BigDecimal.ZERO;
    }

    public static BorrowingDetailBuilder builder() {
        return new BorrowingDetailBuilder();
    }

    public Borrowing getBorrowing() { return borrowing; }
    public void setBorrowing(Borrowing borrowing) { this.borrowing = borrowing; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getReturnedQuantity() { return returnedQuantity; }
    public void setReturnedQuantity(Integer returnedQuantity) { this.returnedQuantity = returnedQuantity; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }

    public static class BorrowingDetailBuilder {
        private Borrowing borrowing;
        private Book book;
        private Integer quantity;
        private Integer returnedQuantity = 0;
        private BigDecimal fineAmount = BigDecimal.ZERO;

        public BorrowingDetailBuilder borrowing(Borrowing borrowing) { this.borrowing = borrowing; return this; }
        public BorrowingDetailBuilder book(Book book) { this.book = book; return this; }
        public BorrowingDetailBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public BorrowingDetailBuilder returnedQuantity(Integer returnedQuantity) { this.returnedQuantity = returnedQuantity; return this; }
        public BorrowingDetailBuilder fineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; return this; }

        public BorrowingDetail build() {
            return new BorrowingDetail(borrowing, book, quantity, returnedQuantity, fineAmount);
        }
    }
}
