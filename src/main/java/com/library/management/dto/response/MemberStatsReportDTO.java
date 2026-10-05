package com.library.management.dto.response;

public class MemberStatsReportDTO {
    private Long memberId;
    private String fullName;
    private String email;
    private Long totalBorrowings;

    public MemberStatsReportDTO() {
    }

    public MemberStatsReportDTO(Long memberId, String fullName, String email, Long totalBorrowings) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.email = email;
        this.totalBorrowings = totalBorrowings;
    }

    public static MemberStatsReportDTOBuilder builder() {
        return new MemberStatsReportDTOBuilder();
    }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getTotalBorrowings() { return totalBorrowings; }
    public void setTotalBorrowings(Long totalBorrowings) { this.totalBorrowings = totalBorrowings; }

    public static class MemberStatsReportDTOBuilder {
        private Long memberId;
        private String fullName;
        private String email;
        private Long totalBorrowings;

        public MemberStatsReportDTOBuilder memberId(Long memberId) { this.memberId = memberId; return this; }
        public MemberStatsReportDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public MemberStatsReportDTOBuilder email(String email) { this.email = email; return this; }
        public MemberStatsReportDTOBuilder totalBorrowings(Long totalBorrowings) { this.totalBorrowings = totalBorrowings; return this; }

        public MemberStatsReportDTO build() {
            return new MemberStatsReportDTO(memberId, fullName, email, totalBorrowings);
        }
    }
}
