# Bài Tập Spring Boot: Hệ Thống Quản Lý Thư Viện (Library Management System)

> **Mức độ:** Junior Plus  
> **Thời lượng ước tính:** 3 đến 5 ngày  
> **Công nghệ chủ đạo:** Spring Boot 3.x, Java 17/21, Spring Data JPA, Thymeleaf, PostgreSQL/MySQL  

---

## 1. Mục Tiêu Học Tập
- **Thiết kế MVC & Routing:** Xây dựng luồng màn hình MVC, controller route và form xử lý nghiệp vụ rõ ràng.
- **Mô hình hóa dữ liệu:** Xây dựng mô hình dữ liệu có quan hệ 1-N (Một - Nhiều) và N-N (Nhiều - Nhiều) thông qua bảng chi tiết.
- **Áp dụng Spring Framework Core:** Sử dụng Spring Data JPA, `@Transactional`, Jakarta Validation và Global Exception Handling.
- **Phân tầng kiến trúc:** Tách bạch rõ ràng giữa Controller, Service, Repository, DTO, Mapper theo đúng trách nhiệm.
- **Database & Testing:** Thực hành viết migration dữ liệu (Flyway/Liquibase), seed data, làm việc với giao diện Thymeleaf và viết Unit Test cơ bản.

---

## 2. Bối Cảnh Bài Toán
Một thư viện nội bộ cần hệ thống quản lý sách, thành viên và quy trình mượn/trả sách. 
- Mỗi thành viên có thể mượn nhiều sách trong một phiếu mượn.
- Hệ thống phải kiểm tra tồn kho sách thực tế, trạng thái hoạt động của thành viên, hạn trả sách (mặc định 14 ngày) và tự động tính tiền phạt khi trả muộn (5.000 VNĐ/ngày/cuốn).

---

## 3. Quy Định Công Nghệ Bắt Buộc

| Nhóm Công Nghệ | Yêu Cầu Cụ Thể |
| :--- | :--- |
| **Ngôn ngữ & Framework** | Java 17 hoặc 21, Spring Boot 3.x |
| **Cơ sở dữ liệu (Database)** | PostgreSQL hoặc MySQL (*Không dùng in-memory DB cho bản nộp chính*) |
| **Persistence Layer** | Spring Data JPA, Hibernate, quản lý giao dịch bằng `@Transactional` |
| **Validation** | `jakarta.validation` với `@Valid` và các annotation phù hợp |
| **Database Migration** | Flyway hoặc Liquibase |
| **Giao diện (Frontend)** | Thymeleaf, Bootstrap hoặc CSS thuần |
| **Kiểm thử (Testing)** | JUnit 5, Mockito (ít nhất phải có unit test cho các Service chính) |

---

## 4. Danh Sách Chức Năng Bắt Buộc

### 4.1 Quản lý Thể loại Sách (Category Management)
- Tạo mới, cập nhật, xóa mềm (soft delete) và xem danh sách thể loại.
- **Ràng buộc:** Tên thể loại không được trùng lặp, không được để rỗng.

### 4.2 Quản lý Sách (Book Management)
- Tạo mới sách bao gồm các thông tin: Mã ISBN, tên sách, tác giả, thể loại, tổng số lượng (`totalQuantity`) và số lượng còn lại (`availableQuantity`).
- Tìm kiếm sách theo tên, tác giả, mã ISBN hoặc thể loại.
- **Ràng buộc:** Không cho phép xóa sách nếu sách đó đang có lượt mượn chưa trả.

### 4.3 Quản lý Thành viên (Member Management)
- Tạo mới, cập nhật, khóa (lock) hoặc mở khóa (unlock) thành viên.
- Email thành viên phải hợp lệ và không được trùng lặp trong hệ thống.
- **Ràng buộc:** Thành viên ở trạng thái bị khóa (`BLOCKED`) không được phép tạo phiếu mượn mới.

### 4.4 Nghiệp vụ Mượn Sách (Borrowing Process)
- Một phiếu mượn có thể đăng ký mượn nhiều cuốn sách khác nhau.
- Kiểm tra tồn kho: Không cho mượn nếu `availableQuantity` không đủ.
- Mặc định hạn trả (`dueDate`) là **14 ngày** kể từ ngày mượn.
- Khi tạo phiếu mượn thành công, hệ thống phải tự động trừ số lượng sách khả dụng (`availableQuantity`).

### 4.5 Nghiệp vụ Trả Sách (Return Process)
- Hỗ trợ trả từng cuốn sách hoặc trả toàn bộ phiếu mượn cùng lúc.
- Khi trả sách, hệ thống cập nhật cộng trả lại số lượng sách khả dụng (`availableQuantity`).
- **Tính tiền phạt:** Nếu trả sau hạn (`dueDate`), tính tiền phạt **5.000 VNĐ/ngày** cho mỗi cuốn sách trả muộn.
- Khi toàn bộ sách trong phiếu đã được trả hết, trạng thái phiếu mượn chuyển thành `RETURNED`.

### 4.6 Báo cáo Thống kê Đơn giản
- Lập danh sách các phiếu mượn quá hạn chưa trả.
- Thống kê Top 5 cuốn sách được mượn nhiều nhất.
- Thống kê tổng số lượt mượn theo từng thành viên.

---

## 5. Mô Hình Dữ Liệu Đề Xuất (Data Model)

| Entity | Trường chính (Fields) | Ghi chú quan hệ |
| :--- | :--- | :--- |
| **Category** | `id`, `name`, `description`, `active` | Một thể loại có thể chứa nhiều sách (1-N). |
| **Book** | `id`, `isbn`, `title`, `author`, `totalQuantity`, `availableQuantity`, `status` | Nhiều sách thuộc về một thể loại (N-1). |
| **Member** | `id`, `fullName`, `email`, `phone`, `status`, `createdAt` | Một thành viên có thể có nhiều phiếu mượn (1-N). |
| **Borrowing** | `id`, `memberId`, `borrowDate`, `dueDate`, `returnedDate`, `status`, `totalFine` | Một phiếu mượn chứa nhiều chi tiết mượn (1-N). |
| **BorrowingDetail** | `id`, `borrowingId`, `bookId`, `quantity`, `returnedQuantity`, `fineAmount` | Bảng chi tiết lưu số lượng mượn, đã trả và tiền phạt riêng. |

---

## 6. Danh Sách Màn Hình & Routes Tối Thiểu

| Màn hình | Route | Controller Action | Ghi chú nghiệp vụ |
| :--- | :--- | :--- | :--- |
| **Danh sách sách** | `/books` | `GET /books` | Hỗ trợ phân trang và tìm kiếm |
| **Form tạo sách** | `/books/new` | `GET /books/new` | Hiển thị form nhập sách |
| **Lưu sách** | `/books` | `POST /books` | Validate ISBN, tên, số lượng |
| **Form sửa sách** | `/books/{id}/edit` | `GET /books/{id}/edit` | Nạp dữ liệu lên form sửa |
| **Danh sách thành viên** | `/members` | `GET /members` | Hỗ trợ lọc theo trạng thái |
| **Form thành viên** | `/members/new` | `GET /members/new` | Validate email không trùng |
| **Tạo phiếu mượn** | `/borrowings/new` | `GET /borrowings/new` | Chọn thành viên và chọn nhiều sách |
| **Lưu phiếu mượn** | `/borrowings` | `POST /borrowings` | Thực thi trong Transaction |
| **Trả sách** | `/borrowings/{id}/return` | `GET/POST` | Hỗ trợ trả từng phần hoặc tất cả |
| **Báo cáo quá hạn** | `/reports/overdue` | `GET /reports/overdue` | Chỉ lấy các phiếu mượn chưa trả hết quá hạn |

---

## 7. Quy Tắc Nghiệp Vụ Bắt Buộc (Business Rules)
1. **Transaction Management:** Toàn bộ thao tác tạo phiếu mượn và trả sách phải được thực hiện trong `@Transactional`.
2. **Data Integrity:** Không bao giờ để `availableQuantity` nhỏ hơn `0` hoặc lớn hơn `totalQuantity`.
3. **Status Check:** 
   - Không cho mượn sách đang ở trạng thái `INACTIVE` hoặc `DELETED`.
   - Không cho thành viên ở trạng thái `BLOCKED` tạo phiếu mượn mới.
4. **Error Handling:** 
   - Lỗi validate trên form phải hiển thị chi tiết tại vị trí trường tương ứng.
   - Lỗi nghiệp vụ phải phản hồi thông báo rõ ràng, dễ hiểu cho người dùng.
5. **Pagination & Sorting:** Các màn hình danh sách bắt buộc phải hỗ trợ phân trang (`page`, `size`) và sắp xếp tối thiểu theo `createdAt` hoặc `id`.

---

## 8. Yêu Cầu Cấu Trúc Project

Ứng dụng cần tổ chức theo phân tầng rõ ràng:
- `controller`: Nhận request, thực hiện validation, trả response/view, không chứa nghiệp vụ phức tạp.
- `service`: Chứa toàn bộ nghiệp vụ chính, quản lý `@Transactional` và tương tác với repository.
- `repository`: Kế thừa `JpaRepository`, thực hiện truy vấn cơ sở dữ liệu.
- `dto` / `form model`: Nhận dữ liệu từ form, thực hiện validation đầu vào; tránh đưa trực tiếp Entity ra ngoài view.
- `entity`: Ánh xạ cấu trúc bảng DB thông qua JPA Annotation.
- `exception`: Chứa Custom Exceptions và `@ControllerAdvice` để xử lý lỗi tập trung.
- `templates`: Chứa các file HTML Thymeleaf theo từng module (`books`, `members`, `borrowings`, `reports`).
- `static`: Chứa các tài nguyên tĩnh như CSS, JS, Images.
- `mapper`: Chuyển đổi giữa Entity và DTO (viết tay hoặc sử dụng MapStruct).

---

## 9. Yêu Cầu Dữ Liệu Khởi Tạo (Seed Data)
Cần chuẩn bị script khởi tạo tối thiểu:
- Ít nhất **5 thể loại** sách.
- Ít nhất **20 cuốn sách** thuộc các thể loại khác nhau.
- Ít nhất **10 thành viên** với các trạng thái khác nhau (`ACTIVE`, `BLOCKED`).
- Ít nhất **5 phiếu mượn mẫu** (bao gồm cả phiếu bình thường, phiếu quá hạn và phiếu đã trả xong).

---

## 10. Hồ Sơ Bài Nộp (Deliverables)
- Source code đầy đủ đẩy lên Git hoặc đóng gói file Zip.
- File `../../../../README.md` mô tả chi tiết cách cài đặt, cấu hình database và khởi chạy ứng dụng.
- File Migration script (Flyway/Liquibase) hoặc file SQL khởi tạo schema.
- Giao diện Thymeleaf hoàn thiện, chạy ổn định khi khởi động.
- *(Điểm cộng)* Dán kèm ảnh chụp màn hình minh họa các luồng nghiệp vụ chính.

---

## 11. Tiêu Chí Chấm Điểm (Evaluation Criteria)

| Tiêu chí | Trọng số | Kỳ vọng đạt được |
| :--- | :---: | :--- |
| **Thiết kế Database** | **20%** | Mối quan hệ chuẩn xác, constraint hợp lý, script migration chạy thành công. |
| **MVC & Phân tầng** | **20%** | Controller mỏng, Service đúng trách nhiệm nghiệp vụ, Form/View tách biệt. |
| **Nghiệp vụ Mượn/Trả** | **25%** | Xử lý chuẩn xác tồn kho, Transaction, tính hạn quá hạn & trả từng phần. |
| **Validation & Error** | **15%** | Validate đầu vào đầy đủ, thông báo lỗi đồng nhất và thân thiện. |
| **Test & Tài liệu** | **15%** | Có Unit Test cho Service chính, file README hướng dẫn rõ ràng. |
| **Code Quality** | **5%** | Code sạch, dễ đọc, tuân thủ naming convention, không trùng lặp code. |

---

## 12. Yêu Cầu Nâng Cao (Optional Upgrades)
- Tích hợp **Spring Security** phân quyền dựa trên JWT/Session cho vai trò `ADMIN` và `LIBRARIAN`.
- Áp dụng **Optimistic Locking** (`@Version`) để xử lý tranh chấp khi nhiều người mượn cuốn sách cuối cùng đồng thời.
- Viết **Scheduled Job** (`@Scheduled`) tự động quét và đánh dấu các phiếu mượn bị quá hạn hàng ngày.
- Đóng gói toàn bộ ứng dụng và database bằng **Docker Compose**.
- Viết **Integration Test** với **Testcontainers**.

---

## 13. Câu Hỏi Review Sau Khi Nộp
1. Vì sao thao tác mượn sách bắt buộc phải chạy trong `@Transactional`?
2. Khi nào nên dùng DTO/Form Model thay vì đưa trực tiếp Entity ra ngoài View?
3. Nếu hai thủ thư cùng bấm mượn cuốn sách cuối cùng tại một thời điểm, hệ thống hiện tại sẽ xử lý ra sao?
4. Sự khác biệt và bài toán thực tế giữa Lazy Loading và Eager Loading trong mô hình dữ liệu này là gì?
5. Bạn sẽ đề xuất giải pháp tối ưu nào cho API Báo cáo khi dữ liệu phiếu mượn tăng lên hàng triệu bản ghi?