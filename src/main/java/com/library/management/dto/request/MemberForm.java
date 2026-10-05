package com.library.management.dto.request;

import com.library.management.enums.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberForm {

    private Long id;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String phone;

    private MemberStatus status = MemberStatus.ACTIVE;

    public MemberForm() {
    }

    public MemberForm(Long id, String fullName, String email, String phone, MemberStatus status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status != null ? status : MemberStatus.ACTIVE;
    }

    public static MemberFormBuilder builder() {
        return new MemberFormBuilder();
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

    public static class MemberFormBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private MemberStatus status = MemberStatus.ACTIVE;

        public MemberFormBuilder id(Long id) { this.id = id; return this; }
        public MemberFormBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public MemberFormBuilder email(String email) { this.email = email; return this; }
        public MemberFormBuilder phone(String phone) { this.phone = phone; return this; }
        public MemberFormBuilder status(MemberStatus status) { this.status = status; return this; }

        public MemberForm build() {
            return new MemberForm(id, fullName, email, phone, status);
        }
    }
}
