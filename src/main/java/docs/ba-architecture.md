# Tài Liệu Phân Tích Nghiệp Vụ (BA) & Kiến Trúc Hệ Thống (System Architecture)

> **Dự án:** Spring Boot Library Management System  
> **Tài liệu tham chiếu:** Bài tập Spring Boot cho Junior Developer  
> **Ngôn ngữ:** Java 17/21 | **Framework:** Spring Boot 3.x | **Database:** PostgreSQL/MySQL  

---

## 1. Tổng Quan Hệ Thống & Phạm Vi Nghiệp Vụ (System Overview & Scope)

Hệ thống Quản lý Mượn/Trả Sách Thư viện Nội bộ được xây dựng nhằm tự động hóa các quy trình quản lý danh mục sách, thành viên thư viện, cũng như kiểm soát chính xác lượng tồn kho sách và công nợ phạt trả muộn.

### 1.1 Đối tượng sử dụng
- **Thủ thư (Librarian) / Admin:** Quản lý thể loại, sách, thành viên, lập phiếu mượn/trả sách, xem báo cáo quá hạn và thống kê.

### 1.2 Phạm vi chức năng
1. **Quản lý Thể loại (Category Management):** Danh mục phân loại sách, hỗ trợ xóa mềm (`soft delete`).
2. **Quản lý Sách (Book Management):** Quản lý thông tin chi tiết, mã ISBN, số lượng tổng (`totalQuantity`), số lượng khả dụng (`availableQuantity`).
3. **Quản lý Thành viên (Member Management):** Thông tin thành viên, email duy nhất, quản lý trạng thái (`ACTIVE`, `BLOCKED`).
4. **Nghiệp vụ Mượn Sách (Borrowing Management):** Lập phiếu mượn nhiều cuốn sách, tự động trừ tồn kho, thiết lập hạn trả mặc định 14 ngày.
5. **Nghiệp vụ Trả Sách (Return Management):** Cho phép trả từng phần hoặc trả toàn bộ, tự động cộng hoàn tồn kho, tự động tính tiền phạt quá hạn (5.000 VNĐ/ngày/cuốn).
6. **Báo cáo & Thống kê (Reporting):** Lọc danh sách quá hạn, Top 5 sách mượn nhiều nhất, thống kê theo thành viên.

---

## 2. Quy Tắc Nghiệp Vụ Chi Tiết (Detailed Business Rules)

```
[Khách / Thành viên] ──> Lập Phiếu Mượn ──> [Kiểm tra Trạng thái Member & Tồn kho Book]
                                                        │
                                          ┌─────────────┴─────────────┐
                                     (Không đủ điều kiện)        (Thỏa điều kiện)
                                          │                           │
                                     [Báo Lỗi Form]              [Tạo Borrowing & Deduct Available Qty]
                                                                      │
                                                                 [Hạn trả: 14 ngày]
```

### BR-01: Quản lý Thể loại
- Tên thể loại là duy nhất (`UNIQUE`), không được để rỗng.
- Thực hiện xóa mềm bằng cờ `active = false` để đảm bảo tính toàn vẹn dữ liệu tham chiếu.

### BR-02: Quản lý Sách & Tồn kho
- Tồn kho luôn đảm bảo bất biến (Invariant):  
  $$0 \le 	ext{availableQuantity} \le 	ext{totalQuantity}$$
- Không cho phép xóa cứng/xóa mềm sách nếu sách đó đang nằm trong ít nhất một phiếu mượn chưa trả xong.
- Tìm kiếm sách hỗ trợ đa tiêu chí: Tên sách, Tác giả, ISBN, Thể loại.

### BR-03: Ràng buộc Thành viên
- Email thành viên là duy nhất và tuân thủ định dạng chuẩn.
- Thành viên bị khóa (`status = BLOCKED`) **không thể** đứng tên tạo phiếu mượn mới.

### BR-04: Luồng Lập Phiếu Mượn
- Một phiếu mượn chứa 1 hoặc nhiều dòng chi tiết (`BorrowingDetail`).
- Với mỗi sách chọn mượn, kiểm tra: `availableQuantity >= quantity_request`.
- Ngay khi tạo phiếu mượn thành công trong giao dịch (`@Transactional`):
  $$	ext{availableQuantity}_{	ext{new}} = 	ext{availableQuantity}_{	ext{old}} - 	ext{quantity}_{	ext{borrowed}}$$
- Ngày mượn (`borrowDate`) = Ngày hiện tại.
- Hạn trả (`dueDate`) = `borrowDate` + 14 ngày.

### BR-05: Luồng Trả Sách & Tính Tiền Phạt
- Cho phép trả từng cuốn hoặc toàn bộ các cuốn trong phiếu mượn.
- Khi nhận trả lại $k$ cuốn sách:
  $$	ext{availableQuantity}_{	ext{new}} = 	ext{availableQuantity}_{	ext{old}} + k$$
- **Tính phạt quá hạn (`fineAmount`):**
  - Nếu `returnedDate > dueDate`:
    $$	ext{Overdue Days} = 	ext{returnedDate} - 	ext{dueDate}$$
    $$	ext{fineAmount} = 	ext{Overdue Days} 	imes 5.000 	ext{ VNĐ} 	imes 	ext{Quantity}$$
  - Ngược lại: `fineAmount = 0 VNĐ`.
- Cập nhật `totalFine` của phiếu mượn bằng tổng `fineAmount` của các chi tiết.
- Khi toàn bộ số lượng sách của phiếu mượn đã được trả đủ, trạng thái phiếu mượn chuyển thành `RETURNED`.

---

## 3. Thiết Kế Cơ Sở Dữ Liệu & Entity Relationship (Data Architecture)

### 3.1 Bảng Chi Tiết Cấu Trúc Các Entity

#### Bảng `categories`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Mã định danh thể loại |
| `name` | VARCHAR(100) | NOT NULL, UNIQUE | Tên thể loại |
| `description` | TEXT | NULLABLE | Mô tả thể loại |
| `active` | BOOLEAN | NOT NULL, DEFAULT true | Cờ cờ xóa mềm |

#### Bảng `books`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Mã định danh sách |
| `category_id` | BIGINT | FOREIGN KEY (categories.id) | Thể loại sách |
| `isbn` | VARCHAR(20) | NOT NULL, UNIQUE | Mã chuẩn quốc tế ISBN |
| `title` | VARCHAR(255) | NOT NULL | Tên sách |
| `author` | VARCHAR(150) | NOT NULL | Tác giả |
| `total_quantity` | INT | NOT NULL, CHECK (>= 0) | Tổng số lượng sách nhập |
| `available_quantity` | INT | NOT NULL, CHECK (>= 0) | Số lượng sách khả dụng |
| `status` | VARCHAR(20) | NOT NULL | Trạng thái (`ACTIVE`, `INACTIVE`) |

#### Bảng `members`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Mã định danh thành viên |
| `full_name` | VARCHAR(150) | NOT NULL | Họ và tên |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE | Email liên hệ |
| `phone` | VARCHAR(20) | NULLABLE | Số điện thoại |
| `status` | VARCHAR(20) | NOT NULL | Trạng thái (`ACTIVE`, `BLOCKED`) |
| `created_at` | TIMESTAMP | NOT NULL | Ngày đăng ký tài khoản |

#### Bảng `borrowings`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Mã phiếu mượn |
| `member_id` | BIGINT | FOREIGN KEY (members.id) | Thành viên mượn |
| `borrow_date` | DATE | NOT NULL | Ngày lập phiếu mượn |
| `due_date` | DATE | NOT NULL | Hạn trả sách (14 ngày) |
| `returned_date` | DATE | NULLABLE | Ngày trả thực tế (khi trả xong) |
| `status` | VARCHAR(20) | NOT NULL | Trạng thái (`BORROWING`, `RETURNED`, `OVERDUE`) |
| `total_fine` | DECIMAL(12,2) | DEFAULT 0.00 | Tổng tiền phạt quá hạn |

#### Bảng `borrowing_details`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Mã dòng chi tiết |
| `borrowing_id` | BIGINT | FOREIGN KEY (borrowings.id) | Mã phiếu mượn liên kết |
| `book_id` | BIGINT | FOREIGN KEY (books.id) | Mã sách mượn |
| `quantity` | INT | NOT NULL | Số lượng mượn |
| `returned_quantity` | INT | DEFAULT 0 | Số lượng đã trả thực tế |
| `fine_amount` | DECIMAL(12,2) | DEFAULT 0.00 | Tiền phạt trên dòng này |

---

## 4. Kiến Trúc Phân Tầng Hệ Thống (Layered Architecture)

Ứng dụng tuân thủ mô hình phân tầng tiêu chuẩn của Spring Boot MVC:

```
┌─────────────────────────────────────────────────────────┐
│              Presentation Layer (Thymeleaf UI)          │
└────────────────────────────┬────────────────────────────┘
                             │  HTTP Request / Response
┌────────────────────────────▼────────────────────────────┐
│                    Controller Layer                     │
│  - BookController, MemberController, BorrowingController │
│  - Receives DTO / Form Model & Performs @Valid          │
└────────────────────────────┬────────────────────────────┘
                             │  DTO / Domain Calls
┌────────────────────────────▼────────────────────────────┐
│                     Service Layer                       │
│  - BookService, MemberService, BorrowingService         │
│  - Handles Core Business Logic & @Transactional          │
└────────────────────────────┬────────────────────────────┘
                             │  JPA Interfaces
┌────────────────────────────▼────────────────────────────┐
│                    Repository Layer                     │
│  - BookRepository, MemberRepository, BorrowingRepository│
│  - Spring Data JPA + Custom Queries                     │
└────────────────────────────┬────────────────────────────┘
                             │  SQL Queries
┌────────────────────────────▼────────────────────────────┐
│                  Relational Database                    │
│                 (PostgreSQL / MySQL)                    │
└─────────────────────────────────────────────────────────┘
```

### 4.1 Chi tiết các phân tầng
1. **Controller Layer:**
   - Đảm nhận tiếp nhận HTTP requests từ client, thực hiện binding dữ liệu vào Form DTO, gọi dịch vụ `@Valid`.
   - Trả về Thymeleaf Template view kèm các Model attribute. Không chứa nghiệp vụ tính toán hay truy vấn database trực tiếp.
2. **Service Layer:**
   - Nơi tập trung toàn bộ Business Logic.
   - Đảm bảo tính nhất quán dữ liệu bằng `@Transactional` trên các phương thức phức hợp (như mượn/trả sách).
3. **Repository Layer:**
   - Sử dụng `JpaRepository` để thao tác CRUD và viết các hàm JPQL/Native Query phục vụ báo cáo.
4. **DTO & Mapper Layer:**
   - Tách biệt dữ liệu nhận từ Client (Form) và Entity dưới Database. Sử dụng Mapper (MapStruct hoặc Custom Mapper) để ánh xạ 2 chiều.
5. **Exception Handling Layer:**
   - Sử dụng `@ControllerAdvice` để bắt các ngoại lệ nghiệp vụ (`BusinessException`, `ResourceNotFoundException`, `InsufficientStockException`) và trả về giao diện thông báo lỗi chuẩn hóa.

---

## 5. Thiết Kế Endpoints & Flow Mapping

| STT | Chức năng | Endpoint Route | HTTP Method | Form DTO / Params | Response View |
| :---: | :--- | :--- | :---: | :--- | :--- |
| 1 | Xem danh sách sách | `/books` | `GET` | `page`, `size`, `keyword`, `categoryId` | `books/list.html` |
| 2 | Mở Form tạo sách | `/books/new` | `GET` | - | `books/form.html` |
| 3 | Lưu sách mới | `/books` | `POST` | `@Valid BookFormDto` | Redirect `/books` hoặc Form lỗi |
| 4 | Mở Form sửa sách | `/books/{id}/edit` | `GET` | `id` | `books/form.html` |
| 5 | Danh sách thành viên | `/members` | `GET` | `page`, `size`, `status` | `members/list.html` |
| 6 | Form tạo thành viên | `/members/new` | `GET` | - | `members/form.html` |
| 7 | Lưu thành viên | `/members` | `POST` | `@Valid MemberFormDto` | Redirect `/members` |
| 8 | Form mượn sách | `/borrowings/new` | `GET` | - | `borrowings/create.html` |
| 9 | Lưu phiếu mượn | `/borrowings` | `POST` | `@Valid BorrowingCreateDto` | Redirect `/borrowings` |
| 10 | Trả sách (Form/Action)| `/borrowings/{id}/return`| `GET/POST` | `borrowingDetailId`, `returnQty` | Redirect `/borrowings/{id}` |
| 11 | Báo cáo quá hạn | `/reports/overdue` | `GET` | `page`, `size` | `reports/overdue.html` |

---

## 6. Giải Pháp Cho Các Điểm Mở Rộng Kiến Trúc (Architecture Upgrades)

### 6.1 Xử lý Tranh chấp Đồng thời (Concurrency Handling)
- Trong kịch bản 2 người dùng mượn cuốn sách cuối cùng cùng lúc:
  - Sử dụng **Optimistic Locking** bằng cách thêm trường `@Version private Long version;` vào Entity `Book`.
  - Khi có tranh chấp ghi dữ liệu, Spring JPA sẽ ném ra `ObjectOptimisticLockingFailureException`, cho phép hệ thống catch và yêu cầu người dùng thử lại.

### 6.2 Phân quyền Spring Security
- Cấu hình Spring Security quản lý 2 vai trò:
  - `LIBRARIAN`: Thực hiện mượn trả sách, xem danh sách.
  - `ADMIN`: Có đầy đủ quyền thêm/sửa/xóa thể loại, sách, khóa thành viên và xem báo cáo.