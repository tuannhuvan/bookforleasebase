# Kế Hoạch Triển Khai & Đóng Gói (Delivery Plan & Roadmap)

> **Dự án:** Spring Boot Library Management System  
> **Thời gian thực hiện:** 3 - 5 Ngày  
> **Đối tượng áp dụng:** Junior Plus Developer  

---

## 1. Lộ Trình Triển Khai Chi Tiết (Project Roadmap)

Lộ trình được thiết kế chi tiết theo chu kỳ 5 ngày làm việc nhằm bảo đảm đầy đủ các yêu cầu bắt buộc và đạt điểm tối đa trên các tiêu chí chấm điểm.

```
[Ngày 1] Init Project & Migration ──> [Ngày 2] Core Modules (Category, Book, Member)
                                                │
[Ngày 5] Test, Docs & Packaging <── [Ngày 3-4] Core Business (Borrowing, Returning, Reports)
```

---

### Giai Đoạn 1: Khởi Tạo Dự Án & Cơ Sở Dữ Liệu (Ngày 1)
- **Mục tiêu:** Dựng khung ứng dụng Spring Boot 3.x, thiết kế CSDL và viết Migration scripts.
- **Các tác vụ chi tiết (WBS):**
  1. Khởi tạo Spring Boot Project thông qua Spring Initializr (Java 17/21, Spring Web, Spring Data JPA, Thymeleaf, Validation, PostgreSQL/MySQL Driver, Flyway/Liquibase).
  2. Định cấu hình `application.yml` / `application.properties` kết nối Database thật.
  3. Viết script Database Migration (`V1__init_schema.sql`) khởi tạo các bảng: `categories`, `books`, `members`, `borrowings`, `borrowing_details`.
  4. Viết script Seed Data (`V2__insert_sample_data.sql`):
     - $\ge 5$ Thể loại sách.
     - $\ge 20$ Cuốn sách thuộc các thể loại.
     - $\ge 10$ Thành viên (bao gồm trạng thái `ACTIVE` và `BLOCKED`).
     - $\ge 5$ Phiếu mượn mẫu (có phiếu quá hạn và phiếu đã trả).

---

### Giai Đoạn 2: Phát Triển Các Module Danh Mục Cơ Bản (Ngày 2)
- **Mục tiêu:** Hoàn thiện phân tầng MVC cho Category, Book và Member.
- **Các tác vụ chi tiết (WBS):**
  1. **Module Category:**
     - Xây dựng Entity `Category`, `CategoryRepository`, `CategoryService`, `CategoryController`.
     - Tạo view Thymeleaf hiển thị danh sách, form tạo mới/chỉnh sửa, hỗ trợ xóa mềm (`active = false`).
  2. **Module Book:**
     - Xây dựng Entity `Book`, DTO (`BookFormDto`), Mapper, `BookRepository`, `BookService`.
     - Cài đặt tính năng tìm kiếm đa tiêu chí và phân trang (`Pageable`).
     - Ràng buộc không cho xóa sách đang có lượt mượn chưa trả.
  3. **Module Member:**
     - Xây dựng Entity `Member`, DTO (`MemberFormDto`), `MemberService`.
     - Bổ sung Custom Validator kiểm tra trùng lặp email và định dạng email.

---

### Giai Đoạn 3: Phát Triển Nghiệp Vụ Mượn - Trả Sách & Báo Cáo (Ngày 3 - 4)
- **Mục tiêu:** Xử lý các quy tắc nghiệp vụ phức tạp cốt lõi, giao dịch `@Transactional` và màn hình báo cáo.
- **Các tác vụ chi tiết (WBS):**
  1. **Nghiệp vụ Mượn sách:**
     - Xây dựng form chọn Thành viên và thêm nhiều Sách vào danh sách mượn.
     - Kiểm tra điều kiện: Thành viên không bị `BLOCKED`, sách mượn có `availableQuantity >= quantity`.
     - Thực hiện trừ `availableQuantity` và lưu `Borrowing` + `BorrowingDetail` trong phương thức dịch vụ có annotation `@Transactional`.
     - Tự động tính `dueDate = borrowDate + 14 ngày`.
  2. **Nghiệp vụ Trả sách:**
     - Xây dựng giao diện xem chi tiết phiếu mượn và chọn trả từng cuốn hoặc toàn bộ.
     - Tự động cộng hoàn trả `availableQuantity`.
     - Tính phạt quá hạn: $5.000 	ext{ VNĐ/ngày/cuốn}$ nếu trả sau `dueDate`.
     - Cập nhật trạng thái phiếu mượn thành `RETURNED` khi trả hết.
  3. **Module Báo cáo:**
     - Xây dựng query JPQL cho: Danh sách phiếu quá hạn, Top 5 sách mượn nhiều nhất, Số lượt mượn theo thành viên.
     - Tạo trang Thymeleaf hiển thị dữ liệu báo cáo sinh động.

---

### Giai Đoạn 4: Testing, Validation, Exception Handling & Đóng Gói (Ngày 5)
- **Mục tiêu:** Viết Unit Test, tối ưu trải nghiệm người dùng, chuẩn bị tài liệu bài nộp.
- **Các tác vụ chi tiết (WBS):**
  1. **Unit Testing:** Viết Unit Test sử dụng JUnit 5 và Mockito kiểm thử các trường hợp thành công và thất bại cho `BorrowingService` và `BookService`.
  2. **Validation & Global Exception Handling:**
     - Hoàn thiện việc hiển thị lỗi validate ngay tại các field trong form Thymeleaf.
     - Bổ sung `@ControllerAdvice` bắt các ngoại lệ nghiệp vụ và hiển thị trang thông báo lỗi thân thiện.
  3. **Soạn thảo tài liệu Bài nộp:**
     - Viết file `../../../../README.md` hoàn chỉnh.
     - Đóng gói Nguồn nguồn Git/Zip và kiểm tra lại toàn bộ ứng dụng.

---

## 2. Danh Mục Bài Nộp & Kiểm Tra Chất Lượng (Deliverables Checklist)

| STT | Hạng mục bài nộp | Trạng thái yêu cầu | Ghi chú kiểm tra |
| :---: | :--- | :---: | :--- |
| 1 | **Source Code** | **Bắt buộc** | Mã nguồn đầy đủ, không dính file rác/build artifact. |
| 2 | **File README.md** | **Bắt buộc** | Hướng dẫn cấu hình DB, các bước chạy app, mô tả API/Route. |
| 3 | **Migration Scripts** | **Bắt buộc** | Script Flyway/Liquibase chạy sạch trên DB trắng. |
| 4 | **Giao diện Thymeleaf** | **Bắt buộc** | Giao diện hoạt động trơn tru, hiển thị lỗi đầy đủ. |
| 5 | **Unit Tests** | **Bắt buộc** | Tối thiểu có test case cho luồng mượn/trả sách. |
| 6 | **Ảnh chụp màn hình** | *Khuyến khích* | Ảnh minh họa các luồng chính dán trong README. |

---

## 3. Ma Trận Đánh Giá & Tiêu Chí Chấm Điểm (Evaluation Matrix)

| Hạng mục | Trọng số | Tiêu chuẩn đánh giá chi tiết |
| :--- | :---: | :--- |
| **1. Thiết kế Database** | **20%** | - Thiết kế đúng chuẩn 3NF, quan hệ 1-N, N-N hợp lý.<br>- Có đầy đủ Constraint (Foreign Key, Unique, Check).<br>- Script Migration tự động chạy mượt mà. |
| **2. MVC & Phân tầng** | **20%** | - Phân định rõ trách nhiệm Controller, Service, Repository, DTO.<br>- Controller không chứa logic tính toán.<br>- Form DTO tách biệt với Entity. |
| **3. Nghiệp vụ Mượn/Trả** | **25%** | - Quản lý tồn kho tuyệt đối chính xác.<br>- Sử dụng `@Transactional` đúng nơi.<br>- Tính đúng hạn trả 14 ngày, tiền phạt 5k/ngày và trả từng phần. |
| **4. Validation & Handling Lỗi** | **15%** | - Validate chặt chẽ từ Annotation trên DTO.<br>- Thông báo lỗi rõ ràng trên UI Thymeleaf.<br>- Đã cấu hình Global Exception Handler. |
| **5. Test & Tài liệu** | **15%** | - Có ít nhất Unit Test cho Service chính dùng Mockito.<br>- File `../../../../README.md` viết chi tiết, dễ dàng setup và chạy ngay. |
| **6. Code Quality** | **5%** | - Code sạch, đặt tên biến/hàm theo chuẩn Java Naming Convention.<br>- Không có code thừa hoặc trùng lặp quá mức. |

---

## 4. Kế Hoạch Phát Triển Nâng Cao (Optional Upgrades Roadmap)

Nếu còn thời gian sau khi hoàn thành các chức năng bắt buộc:
1. **Spring Security Integration (0.5 ngày):**
   - Cấu hình Form-based Authentication hoặc JWT.
   - Phân quyền theo Role: `ADMIN` (toàn quyền), `LIBRARIAN` (chỉ thao tác mượn/trả).
2. **Optimistic Locking (0.25 ngày):**
   - Thêm cờ `@Version` để ngăn chặn hiện tượng tranh chấp tồn kho đồng thời.
3. **Scheduled Job Quá Hạn (0.25 ngày):**
   - Tạo `@Scheduled(cron = "0 0 0 * * ?")` để tự động cập nhật trạng thái phiếu quá hạn.
4. **Docker Compose Packaging (0.25 ngày):**
   - Viết `Dockerfile` cho ứng dụng Spring Boot và `docker-compose.yml` ghép nối ứng dụng với PostgreSQL/MySQL.