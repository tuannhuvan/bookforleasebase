package com.library.management.dto.response;

import com.library.management.enums.MemberStatus;

import java.time.LocalDateTime;

public class MemberDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private MemberStatus status;
    private LocalDateTime createdAt;

    public MemberDTO() {
    }

    public MemberDTO(Long id, String fullName, String email, String phone, MemberStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static MemberDTOBuilder builder() {
        return new MemberDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class MemberDTOBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private MemberStatus status;
        private LocalDateTime createdAt;

        public MemberDTOBuilder id(Long id) { this.id = id; return this; }
        public MemberDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public MemberDTOBuilder email(String email) { this.email = email; return this; }
        public MemberDTOBuilder phone(String phone) { this.phone = phone; return this; }
        public MemberDTOBuilder status(MemberStatus status) { this.status = status; return this; }
        public MemberDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public MemberDTO build() {
            return new MemberDTO(id, fullName, email, phone, status, createdAt);
        }
    }
}
