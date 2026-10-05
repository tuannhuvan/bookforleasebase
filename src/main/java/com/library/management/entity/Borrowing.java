package com.library.management.entity;

import com.library.management.enums.BorrowingStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "borrowings")
public class Borrowing extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_date")
    private LocalDate returnedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BorrowingStatus status;

    @Column(name = "total_fine", precision = 12, scale = 2)
    private BigDecimal totalFine = BigDecimal.ZERO;

    @OneToMany(mappedBy = "borrowing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowingDetail> details = new ArrayList<>();

    public Borrowing() {
    }

    public Borrowing(Member member, LocalDate borrowDate, LocalDate dueDate, LocalDate returnedDate, BorrowingStatus status, BigDecimal totalFine, List<BorrowingDetail> details) {
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
        this.status = status;
        this.totalFine = totalFine != null ? totalFine : BigDecimal.ZERO;
        if (details != null) this.details = details;
    }

    public static BorrowingBuilder builder() {
        return new BorrowingBuilder();
    }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnedDate() { return returnedDate; }
    public void setReturnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; }
    public BorrowingStatus getStatus() { return status; }
    public void setStatus(BorrowingStatus status) { this.status = status; }
    public BigDecimal getTotalFine() { return totalFine; }
    public void setTotalFine(BigDecimal totalFine) { this.totalFine = totalFine; }
    public List<BorrowingDetail> getDetails() { return details; }
    public void setDetails(List<BorrowingDetail> details) { this.details = details; }

    public static class BorrowingBuilder {
        private Member member;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnedDate;
        private BorrowingStatus status;
        private BigDecimal totalFine = BigDecimal.ZERO;
        private List<BorrowingDetail> details = new ArrayList<>();

        public BorrowingBuilder member(Member member) { this.member = member; return this; }
        public BorrowingBuilder borrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; return this; }
        public BorrowingBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public BorrowingBuilder returnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; return this; }
        public BorrowingBuilder status(BorrowingStatus status) { this.status = status; return this; }
        public BorrowingBuilder totalFine(BigDecimal totalFine) { this.totalFine = totalFine; return this; }
        public BorrowingBuilder details(List<BorrowingDetail> details) { this.details = details; return this; }

        public Borrowing build() {
            return new Borrowing(member, borrowDate, dueDate, returnedDate, status, totalFine, details);
        }
    }
}
