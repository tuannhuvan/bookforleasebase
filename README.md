# 📚 Spring Boot Library Management System (Hệ Thống Quản Lý Thư Viện)

Dự án Xây dựng Hệ thống Quản lý Mượn/Trả Sách Thư viện Nội bộ dựa trên **Spring Boot 3.x**, **Thymeleaf**, **Spring Data JPA** và **Flyway Database Migration**.

---

## 🚀 1. Tổng Quan Kiến Trúc & Công Nghệ

- **Framework Core:** Spring Boot `3.2.5` (Java 17/21)
- **Frontend / Presentation:** Thymeleaf + Bootstrap 5 + FontAwesome 6 (Responsive UI)
- **Data Access Layer:** Spring Data JPA + Hibernate (JPA Specifications cho dynamic search)
- **Database:** H2 In-Memory (Chế độ tương thích MySQL/PostgreSQL cho môi trường dev/test) / MySQL / PostgreSQL Driver
- **Database Migration:** Flyway (`V1__init_schema.sql`, `V2__insert_sample_data.sql`)
- **Validation:** `jakarta.validation` (`@Valid`, `@NotBlank`, `@Email`, `@Min`)
- **Concurrency Control:** Optimistic Locking với `@Version` trên Entity `Book`
- **Testing:** JUnit 5 + Mockito Unit Tests

---

## 🛠️ 2. Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### Yêu cầu môi trường
- **Java Development Kit (JDK):** JDK 17 hoặc JDK 21+
- **Apache Maven:** Đã bao gồm Maven Wrapper (`mvnw` / `mvnw.cmd`)

### Các bước khởi chạy

1. **Clone / Giải nén mã nguồn dự án:**
   ```bash
   cd bookforleasebase
   ```

2. **Chạy ứng dụng với Maven Wrapper:**
   - Trên **Windows (PowerShell / CMD)**:
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```
   - Trên **Linux / macOS**:
     ```bash
     ./mvnw spring-boot:run
     ```

3. **Truy cập ứng dụng:**
   - **Giao diện Web UI:** `http://localhost:8080`
   - **H2 Database Console:** `http://localhost:8080/h2-console`
     - *JDBC URL:* `jdbc:h2:mem:librarydb`
     - *User:* `sa`
     - *Password:* (để trống)

---

## 📋 3. Quy Tắc Nghiệp Vụ Cốt Lõi (Business Rules)

- **BR-01 (Quản lý Thể loại):** Tên thể loại duy nhất. Hỗ trợ **Xóa Mềm** (`active = false`).
- **BR-02 (Quản lý Sách & Tồn kho):** Tồn kho luôn đảm bảo $0 \le \text{availableQuantity} \le \text{totalQuantity}$. Không cho phép xóa sách nếu sách đó đang nằm trong phiếu mượn chưa trả hết.
- **BR-03 (Thành viên):** Email thành viên duy nhất. Thành viên bị khóa (`status = BLOCKED`) **không được phép** lập phiếu mượn mới.
- **BR-04 (Luồng Lập Phiếu Mượn):** 
  - Mặc định hạn trả (`dueDate`) = `borrowDate + 14 ngày`.
  - Thực thi trong giao dịch `@Transactional`, tự động trừ `availableQuantity` của sách.
- **BR-05 (Luồng Trả Sách & Tính Tiền Phạt):**
  - Hỗ trợ trả từng phần hoặc trả toàn bộ.
  - Tự động hoàn trả tồn kho `availableQuantity`.
  - Phạt quá hạn: **5.000 VNĐ / ngày / cuốn** nếu `returnedDate > dueDate`.

---

## 🛣️ 4. Danh Sách Endpoint & Form Routes

| STT | Chức Năng | Route Endpoint | HTTP Method | View Template |
| :---: | :--- | :--- | :---: | :--- |
| 1 | Trang Chủ / Dashboard | `/` | `GET` | `home.html` |
| 2 | Danh sách thể loại | `/categories` | `GET` | `categories/list.html` |
| 3 | Form tạo/sửa thể loại | `/categories/new`, `/categories/{id}/edit` | `GET` | `categories/form.html` |
| 4 | Danh sách sách & Lọc | `/books` | `GET` | `books/list.html` |
| 5 | Chi tiết sách | `/books/{id}` | `GET` | `books/detail.html` |
| 6 | Form tạo/sửa sách | `/books/new`, `/books/{id}/edit` | `GET` | `books/form.html` |
| 7 | Danh sách thành viên | `/members` | `GET` | `members/list.html` |
| 8 | Form tạo/sửa thành viên | `/members/new`, `/members/{id}/edit` | `GET` | `members/form.html` |
| 9 | Khóa/Mở khóa thành viên| `/members/{id}/toggle-status` | `POST` | Redirect `/members` |
| 10 | Danh sách phiếu mượn | `/borrowings` | `GET` | `borrowings/list.html` |
| 11 | Lập phiếu mượn mới | `/borrowings/new` | `GET / POST` | `borrowings/create.html` |
| 12 | Chi tiết phiếu mượn | `/borrowings/{id}` | `GET` | `borrowings/detail.html` |
| 13 | Form Trả Sách | `/borrowings/{id}/return` | `GET / POST` | `borrowings/return.html` |
| 14 | Báo cáo phiếu quá hạn | `/reports/overdue` | `GET` | `reports/overdue.html` |
| 15 | Top sách mượn nhiều | `/reports/top-books` | `GET` | `reports/top-books.html` |
| 16 | Thống kê theo thành viên| `/reports/member-stats` | `GET` | `reports/member-stats.html` |

---

## 🧪 5. Chạy Kiểm Thử (Unit Testing)

Chạy bộ test suite tự động cho các Service cốt lõi (BorrowingService, BookService):

```powershell
.\mvnw.cmd clean test
```

---
*Dự án hoàn thiện theo đúng tài liệu BA & Delivery Plan cho Junior Plus Spring Boot Developer.*
