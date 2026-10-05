package com.library.management.dto.response;

import com.library.management.enums.BorrowingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private BorrowingStatus status;
    private BigDecimal totalFine;
    private long overdueDays;
    private List<BorrowingDetailDTO> details = new ArrayList<>();

    public BorrowingDTO() {
    }

    public BorrowingDTO(Long id, Long memberId, String memberName, String memberEmail, LocalDate borrowDate, LocalDate dueDate, LocalDate returnedDate, BorrowingStatus status, BigDecimal totalFine, long overdueDays, List<BorrowingDetailDTO> details) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
        this.status = status;
        this.totalFine = totalFine;
        this.overdueDays = overdueDays;
        if (details != null) this.details = details;
    }

    public static BorrowingDTOBuilder builder() {
        return new BorrowingDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }
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
    public long getOverdueDays() { return overdueDays; }
    public void setOverdueDays(long overdueDays) { this.overdueDays = overdueDays; }
    public List<BorrowingDetailDTO> getDetails() { return details; }
    public void setDetails(List<BorrowingDetailDTO> details) { this.details = details; }

    public static class BorrowingDTOBuilder {
        private Long id;
        private Long memberId;
        private String memberName;
        private String memberEmail;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnedDate;
        private BorrowingStatus status;
        private BigDecimal totalFine;
        private long overdueDays;
        private List<BorrowingDetailDTO> details = new ArrayList<>();

        public BorrowingDTOBuilder id(Long id) { this.id = id; return this; }
        public BorrowingDTOBuilder memberId(Long memberId) { this.memberId = memberId; return this; }
        public BorrowingDTOBuilder memberName(String memberName) { this.memberName = memberName; return this; }
        public BorrowingDTOBuilder memberEmail(String memberEmail) { this.memberEmail = memberEmail; return this; }
        public BorrowingDTOBuilder borrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; return this; }
        public BorrowingDTOBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public BorrowingDTOBuilder returnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; return this; }
        public BorrowingDTOBuilder status(BorrowingStatus status) { this.status = status; return this; }
        public BorrowingDTOBuilder totalFine(BigDecimal totalFine) { this.totalFine = totalFine; return this; }
        public BorrowingDTOBuilder overdueDays(long overdueDays) { this.overdueDays = overdueDays; return this; }
        public BorrowingDTOBuilder details(List<BorrowingDetailDTO> details) { this.details = details; return this; }

        public BorrowingDTO build() {
            return new BorrowingDTO(id, memberId, memberName, memberEmail, borrowDate, dueDate, returnedDate, status, totalFine, overdueDays, details);
        }
    }
}
