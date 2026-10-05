package com.library.management.entity;

import com.library.management.enums.MemberStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Borrowing> borrowings = new ArrayList<>();

    public Member() {
    }

    public Member(String fullName, String email, String phone, MemberStatus status, List<Borrowing> borrowings) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        if (borrowings != null) this.borrowings = borrowings;
    }

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }
    public List<Borrowing> getBorrowings() { return borrowings; }
    public void setBorrowings(List<Borrowing> borrowings) { this.borrowings = borrowings; }

    public static class MemberBuilder {
        private String fullName;
        private String email;
        private String phone;
        private MemberStatus status;
        private List<Borrowing> borrowings = new ArrayList<>();

        public MemberBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public MemberBuilder email(String email) { this.email = email; return this; }
        public MemberBuilder phone(String phone) { this.phone = phone; return this; }
        public MemberBuilder status(MemberStatus status) { this.status = status; return this; }
        public MemberBuilder borrowings(List<Borrowing> borrowings) { this.borrowings = borrowings; return this; }

        public Member build() {
            return new Member(fullName, email, phone, status, borrowings);
        }
    }
}
