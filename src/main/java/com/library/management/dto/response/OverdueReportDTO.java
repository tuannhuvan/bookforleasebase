package com.library.management.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OverdueReportDTO {
    private Long borrowingId;
    private String memberName;
    private String memberEmail;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private long overdueDays;
    private BigDecimal estimatedFine;

    public OverdueReportDTO() {
    }

    public OverdueReportDTO(Long borrowingId, String memberName, String memberEmail, LocalDate borrowDate, LocalDate dueDate, long overdueDays, BigDecimal estimatedFine) {
        this.borrowingId = borrowingId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.overdueDays = overdueDays;
        this.estimatedFine = estimatedFine;
    }

    public static OverdueReportDTOBuilder builder() {
        return new OverdueReportDTOBuilder();
    }

    public Long getBorrowingId() { return borrowingId; }
    public void setBorrowingId(Long borrowingId) { this.borrowingId = borrowingId; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public long getOverdueDays() { return overdueDays; }
    public void setOverdueDays(long overdueDays) { this.overdueDays = overdueDays; }
    public BigDecimal getEstimatedFine() { return estimatedFine; }
    public void setEstimatedFine(BigDecimal estimatedFine) { this.estimatedFine = estimatedFine; }

    public static class OverdueReportDTOBuilder {
        private Long borrowingId;
        private String memberName;
        private String memberEmail;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private long overdueDays;
        private BigDecimal estimatedFine;

        public OverdueReportDTOBuilder borrowingId(Long borrowingId) { this.borrowingId = borrowingId; return this; }
        public OverdueReportDTOBuilder memberName(String memberName) { this.memberName = memberName; return this; }
        public OverdueReportDTOBuilder memberEmail(String memberEmail) { this.memberEmail = memberEmail; return this; }
        public OverdueReportDTOBuilder borrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; return this; }
        public OverdueReportDTOBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public OverdueReportDTOBuilder overdueDays(long overdueDays) { this.overdueDays = overdueDays; return this; }
        public OverdueReportDTOBuilder estimatedFine(BigDecimal estimatedFine) { this.estimatedFine = estimatedFine; return this; }

        public OverdueReportDTO build() {
            return new OverdueReportDTO(borrowingId, memberName, memberEmail, borrowDate, dueDate, overdueDays, estimatedFine);
        }
    }
}
