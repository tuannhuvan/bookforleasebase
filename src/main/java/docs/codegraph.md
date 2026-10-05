# Sơ Đồ Mã Nguồn & Kiến Trúc Thành Phần (Code Graph & Architecture)

Tài liệu **Code Graph (`codegraph.md`)** cung cấp bản đồ cấu trúc toàn diện của dự án **Spring Boot Library Management System**, bao gồm sơ đồ phân tầng (Layered Architecture), cây thư mục package, biểu đồ lớp (Class Diagram), biểu đồ chuỗi tương tác (Sequence Call Graph) và ma trận ánh xạ các thành phần từ Controller đến Database.

---

## 1. Cấu Trúc Thư Mục & Tổ Chức Package (Directory & Package Layout)

Dự án tuân thủ cấu trúc chuẩn **Maven / Spring Boot 3.x**, chia tầng rõ ràng theo nguyên lý **Single Responsibility Principle (SRP)**:

```text
com.library.management
├── LibraryApplication.java                # Main Entry Point Spring Boot
├── config                                  # Cấu hình hệ thống
│   ├── JpaAuditingConfig.java             # Cấu hình @EnableJpaAuditing (createdAt, updatedAt)
│   ├── MvcConfig.java                     # Cấu hình WebMvc, Interceptor, Resource Handlers
│   └── OpenAPIConfig.java                 # Cấu hình Swagger/OpenAPI 3.0 Documentation
├── controller                             # Tầng Controller (Spring MVC & Rest API)
│   ├── BookController.java                # Quản lý Sách (GET, POST, Form MVC)
│   ├── CategoryController.java            # Quản lý Thể loại
│   ├── MemberController.java              # Quản lý Thành viên
│   ├── BorrowingController.java           # Quản lý Mượn/Trả Sách (Core Business)
│   └── ReportController.java              # Quản lý Báo cáo & Thống kê
├── service                                # Tầng Service (Xử lý nghiệp vụ & Transaction)
│   ├── BookService.java                   # Interface Quản lý Sách
│   ├── CategoryService.java               # Interface Quản lý Thể loại
│   ├── MemberService.java                 # Interface Quản lý Thành viên
│   ├── BorrowingService.java              # Interface Nghiệp vụ Mượn/Trả Sách
│   ├── ReportService.java                 # Interface Báo cáo & Thống kê
│   └── impl                               # Lớp triển khai (Implementations)
│       ├── BookServiceImpl.java
│       ├── CategoryServiceImpl.java
│       ├── MemberServiceImpl.java
│       ├── BorrowingServiceImpl.java     # Thao tác @Transactional, Fine Calculation
│       └── ReportServiceImpl.java
├── repository                             # Tầng Repository (Spring Data JPA)
│   ├── BookRepository.java                # JpaRepository<Book, Long>, Custom Queries
│   ├── CategoryRepository.java            # JpaRepository<Category, Long>
│   ├── MemberRepository.java              # JpaRepository<Member, Long>
│   ├── BorrowingRepository.java           # JpaRepository<Borrowing, Long>
│   └── BorrowingDetailRepository.java     # JpaRepository<BorrowingDetail, Long>
├── entity                                 # Tầng JPA Entities (Domain Models)
│   ├── BaseEntity.java                    # Mẫu Entity cơ sở (id, createdAt, updatedAt)
│   ├── Category.java                      # Bảng categories
│   ├── Book.java                          # Bảng books (@Version Optimistic Lock)
│   ├── Member.java                        # Bảng members
│   ├── Borrowing.java                     # Bảng borrowings
│   └── BorrowingDetail.java               # Bảng borrowing_details
├── dto                                    # Data Transfer Objects & Form Models
│   ├── request
│   │   ├── BookForm.java                  # Form tạo/sửa sách (@Valid constraints)
│   │   ├── MemberForm.java                # Form tạo/sửa thành viên
│   │   ├── CategoryForm.java              # Form tạo thể loại
│   │   ├── BorrowingRequest.java          # Form tạo phiếu mượn (memberId, bookItems)
│   │   └── ReturnRequest.java             # Form trả sách (borrowingId, returnItems)
│   └── response
│       ├── BookDTO.java                   # Response hiển thị Sách
│       ├── MemberDTO.java                 # Response hiển thị Thành viên
│       ├── BorrowingDTO.java              # Response hiển thị Phiếu mượn
│       ├── BorrowingDetailDTO.java        # Response dòng chi tiết phiếu
│       └── OverdueReportDTO.java          # Response DTO báo cáo quá hạn
├── mapper                                 # Chuyển đổi Entity <-> DTO (MapStruct / Manual)
│   ├── BookMapper.java
│   ├── MemberMapper.java
│   ├── CategoryMapper.java
│   └── BorrowingMapper.java
├── enums                                  # Định nghĩa hằng số Enum
│   ├── BookStatus.java                    # AVAILABLE, INACTIVE, DELETED
│   ├── MemberStatus.java                  # ACTIVE, BLOCKED
│   └── BorrowingStatus.java               # BORROWED, PARTIAL_RETURNED, RETURNED, OVERDUE
├── exception                              # Xử lý Lỗi & Ngoại lệ Global
│   ├── ResourceNotFoundException.java     # Lỗi không tìm thấy dữ liệu (404)
│   ├── BusinessRuleException.java         # Lỗi vi phạm quy tắc nghiệp vụ (400)
│   ├── OutOfStockException.java           # Lỗi hết sách tồn kho
│   ├── InsufficientPermissionException.java
│   └── GlobalExceptionHandler.java        # @ControllerAdvice xử lý lỗi cho Thymeleaf & Rest
└── specification                          # JPA Specifications cho Dynamic Search
    ├── BookSpecification.java             # Dynamic search theo title, author, isbn, category
    └── BorrowingSpecification.java        # Dynamic search theo member, status, date
```

---

## 2. Biểu Đồ Phụ Thuộc Thành Phần (Component Dependency Graph)

Sơ đồ mô tả luồng phụ thuộc dữ liệu và điều hướng giữa các thành phần trong hệ thống:

```mermaid
graph TD
    %% Subgraphs cho các Tầng
    subgraph Client_View [Tầng View / Client]
        UI[Thymeleaf Templates / HTML Forms]
    end

    subgraph Controller_Layer [Tầng Controller]
        BC[BookController]
        MC[MemberController]
        BWC[BorrowingController]
        RC[ReportController]
        GEH[GlobalExceptionHandler]
    end

    subgraph Service_Layer [Tầng Service]
        BS[BookService / BookServiceImpl]
        MS[MemberService / MemberServiceImpl]
        BWS[BorrowingService / BorrowingServiceImpl]
        RS[ReportService / ReportServiceImpl]
    end

    subgraph Mapping_Validation [Tầng Support / Mapper / DTO]
        MAP[BookMapper / BorrowingMapper]
        VAL[Jakarta Validation @Valid]
    end

    subgraph Repository_Layer [Tầng Repository - Spring Data JPA]
        BR[BookRepository]
        MR[MemberRepository]
        BWR[BorrowingRepository]
        BDR[BorrowingDetailRepository]
    end

    subgraph Database_Layer [Cơ Sở Dữ Liệu]
        DB[(PostgreSQL / MySQL)]
    end

    %% Tương tác HTTP & Validation
    UI -->|HTTP GET/POST + Form Data| Controller_Layer
    Controller_Layer -->|Validate Đầu Vào| VAL
    Controller_Layer -->|Chuyển Đổi DTO/Form| MAP

    %% Gọi từ Controller sang Service
    BC --> BS
    MC --> MS
    BWC --> BWS
    RC --> RS

    %% Xử lý ngoại lệ
    BS -.->|Throw BusinessRuleException| GEH
    BWS -.->|Throw OutOfStockException| GEH
    GEH -->|Render Error View| UI

    %% Tương tác Service sang Repository
    BS --> BR
    MS --> MR
    BWS --> BR
    BWS --> MR
    BWS --> BWR
    BWS --> BDR
    RS --> BWR

    %% Tương tác Repository sang Database
    BR --> DB
    MR --> DB
    BWR --> DB
    BDR --> DB
```

---

## 3. Biểu Đồ Lớp Entities & JPA Relationships (Class Diagram)

Các Entity chính và mối quan hệ giữa chúng trong Spring Data JPA:

```mermaid
classDiagram
    class BaseEntity {
        <<Abstract>>
        +Long id
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }

    class Category {
        +String name
        +String description
        +Boolean active
        +List~Book~ books
    }

    class Book {
        +String isbn
        +String title
        +String author
        +Integer totalQuantity
        +Integer availableQuantity
        +BookStatus status
        +Long version
        +Category category
    }

    class Member {
        +String fullName
        +String email
        +String phone
        +MemberStatus status
        +List~Borrowing~ borrowings
    }

    class Borrowing {
        +LocalDate borrowDate
        +LocalDate dueDate
        +LocalDate returnedDate
        +BorrowingStatus status
        +BigDecimal totalFine
        +Member member
        +List~BorrowingDetail~ details
    }

    class BorrowingDetail {
        +Integer quantity
        +Integer returnedQuantity
        +BigDecimal fineAmount
        +Borrowing borrowing
        +Book book
    }

    class BookStatus {
        <<Enumeration>>
        AVAILABLE
        INACTIVE
        DELETED
    }

    class MemberStatus {
        <<Enumeration>>
        ACTIVE
        BLOCKED
    }

    class BorrowingStatus {
        <<Enumeration>>
        BORROWED
        PARTIAL_RETURNED
        RETURNED
        OVERDUE
    }

    BaseEntity <|-- Category
    BaseEntity <|-- Book
    BaseEntity <|-- Member
    BaseEntity <|-- Borrowing
    BaseEntity <|-- BorrowingDetail

    Category "1" -- "0..*" Book : contains
    Member "1" -- "0..*" Borrowing : places
    Borrowing "1" *-- "1..*" BorrowingDetail : consists_of
    Book "1" -- "0..*" BorrowingDetail : referenced_in

    Book --> BookStatus
    Member --> MemberStatus
    Borrowing --> BorrowingStatus
```

---

## 4. Luồng Thực Thi Chi Tiết & Biểu Đồ Chuỗi (Sequence Call Graphs)

### 4.1. Luồng Tạo Phiếu Mượn Sách (`POST /borrowings`)

Mô tả quá trình kiểm tra ràng buộc nghiệp vụ, trừ tồn kho và ghi nhận phiếu mượn trong `@Transactional`:

```mermaid
sequenceDiagram
    autonumber
    actor User as Thủ thư / Admin
    participant View as Thymeleaf Form
    participant Ctrl as BorrowingController
    participant Svc as BorrowingServiceImpl
    participant MemRepo as MemberRepository
    participant BookRepo as BookRepository
    participant BorrRepo as BorrowingRepository
    participant DB as Database (PostgreSQL/MySQL)

    User->>View: Điền thông tin (Member ID, Danh sách Book ID & Số lượng)
    View->>Ctrl: POST /borrowings (BorrowingRequest)
    Ctrl->>Ctrl: Validate @Valid (BorrowingRequest)
    alt Validation Failed
        Ctrl-->>View: Trả về View borrowings/new với lỗi
    end

    Ctrl->>Svc: createBorrowing(BorrowingRequest)
    Note over Svc: Bắt đầu Transaction (@Transactional)

    Svc->>MemRepo: findById(memberId)
    MemRepo-->>Svc: Member entity
    alt Member bị BLOCKED hoặc không tồn tại
        Svc-->>Ctrl: Throw BusinessRuleException("Thành viên bị khóa")
        Ctrl-->>View: Hiển thị thông báo lỗi trên UI
    end

    loop Đối với từng cuốn sách mượn
        Svc->>BookRepo: findByIdForUpdate(bookId)
        BookRepo-->>Svc: Book entity
        alt availableQuantity < requestedQuantity hoặc Status != AVAILABLE
            Svc-->>Ctrl: Throw OutOfStockException("Sách không đủ tồn kho")
            Ctrl-->>View: Trả về màn hình mượn kèm thông báo lỗi
        end
        Note over Svc: Trừ tồn kho: availableQuantity -= quantity
        Svc->>BookRepo: save(book)
    end

    Note over Svc: Thiết lập: borrowDate = Today, dueDate = Today + 14 days<br/>status = BORROWED, totalFine = 0
    Svc->>BorrRepo: save(borrowing)
    BorrRepo->>DB: INSERT INTO borrowings & borrowing_details, UPDATE books
    DB-->>BorrRepo: OK
    Note over Svc: Commit Transaction thành công
    Svc-->>Ctrl: BorrowingDTO
    Ctrl-->>View: Redirect /borrowings/{id} (Hiển thị thông tin phiếu mượn)
```

---

### 4.2. Luồng Trả Sách & Tính Phí Quá Hạn (`POST /borrowings/{id}/return`)

Mô tả quy trình xử lý trả sách từng phần/toàn bộ, tính tiền phạt $5.000$ VNĐ/ngày/cuốn quá hạn và hoàn lại tồn kho:

```mermaid
sequenceDiagram
    autonumber
    actor User as Thủ thư
    participant View as Thymeleaf View
    participant Ctrl as BorrowingController
    participant Svc as BorrowingServiceImpl
    participant BorrRepo as BorrowingRepository
    participant BookRepo as BookRepository
    participant DB as Database

    User->>View: Chọn phiếu mượn & nhập số lượng trả từng sách
    View->>Ctrl: POST /borrowings/{id}/return (ReturnRequest)
    Ctrl->>Svc: processReturn(borrowingId, ReturnRequest)
    Note over Svc: Bắt đầu Transaction (@Transactional)

    Svc->>BorrRepo: findByIdWithDetails(borrowingId)
    BorrRepo-->>Svc: Borrowing entity kèm details

    loop Lặp qua từng item trả trong ReturnRequest
        Note over Svc: Tính toán số ngày quá hạn = max(0, Today - dueDate)
        Note over Svc: Phí phạt = OverdueDays * 5.000 VND * returnQty
        Note over Svc: Cập nhật returnedQuantity += returnQty
        Note over Svc: Cộng hoàn tồn kho Book: availableQuantity += returnQty
        Svc->>BookRepo: save(book)
    end

    Note over Svc: Cập nhật totalFine = sum(fineAmount)
    alt Tất cả sách trong phiếu đã trả đủ
        Note over Svc: status = RETURNED, returnedDate = Today
    else Còn sách chưa trả hết
        Note over Svc: status = PARTIAL_RETURNED
    end

    Svc->>BorrRepo: save(borrowing)
    BorrRepo->>DB: UPDATE borrowings, borrowing_details, books
    DB-->>BorrRepo: OK
    Note over Svc: Commit Transaction
    Svc-->>Ctrl: BorrowingDTO (kèm tổng phí phạt)
    Ctrl-->>View: Redirect /borrowings/{id} với thông báo thành công
```

---

## 5. Ma Trận Chi Tiết Các Lớp Trong Hệ Thống (Class Specification Matrix)

### 5.1. Tầng Controllers & Views

| Tên Class Controller | Dynamic Annotations | Services Phụ Thuộc Injection | Các Routes Xử Lý Chính | View Template Tương Ứng |
| :--- | :--- | :--- | :--- | :--- |
| **`BookController`** | `@Controller`<br>`@RequestMapping("/books")` | `BookService`<br>`CategoryService` | `GET /books`<br>`GET /books/new`<br>`POST /books`<br>`GET /books/{id}/edit`<br>`POST /books/{id}` | `books/list.html`<br>`books/form.html`<br>`books/detail.html` |
| **`MemberController`** | `@Controller`<br>`@RequestMapping("/members")` | `MemberService` | `GET /members`<br>`GET /members/new`<br>`POST /members`<br>`POST /members/{id}/toggle-status` | `members/list.html`<br>`members/form.html` |
| **`BorrowingController`** | `@Controller`<br>`@RequestMapping("/borrowings")` | `BorrowingService`<br>`MemberService`<br>`BookService` | `GET /borrowings`<br>`GET /borrowings/new`<br>`POST /borrowings`<br>`GET /borrowings/{id}`<br>`POST /borrowings/{id}/return` | `borrowings/list.html`<br>`borrowings/new.html`<br>`borrowings/detail.html`<br>`borrowings/return.html` |
| **`ReportController`** | `@Controller`<br>`@RequestMapping("/reports")` | `ReportService` | `GET /reports/overdue`<br>`GET /reports/top-books`<br>`GET /reports/member-stats` | `reports/overdue.html`<br>`reports/top-books.html` |

---

### 5.2. Tầng Services & Repositories

| Interface Service | Implementation Class | Annotation Key | Repositories Inject | Trách Nhiệm Nghiệp Vụ Chính |
| :--- | :--- | :--- | :--- | :--- |
| **`BookService`** | `BookServiceImpl` | `@Service`<br>`@Transactional(readOnly)` | `BookRepository`<br>`CategoryRepository` | CRUD Sách, lọc phân trang, kiểm tra mã ISBN trùng, chuyển đổi trạng thái. |
| **`MemberService`** | `MemberServiceImpl` | `@Service`<br>`@Transactional(readOnly)` | `MemberRepository` | CRUD Thành viên, validate email độc nhất, khóa/mở khóa tài khoản. |
| **`BorrowingService`** | `BorrowingServiceImpl` | `@Service`<br>`@Transactional` | `BorrowingRepository`<br>`BorrowingDetailRepository`<br>`BookRepository`<br>`MemberRepository` | Tạo phiếu mượn (trừ tồn kho), trả sách từng phần (cộng tồn kho), tính phí phạt quá hạn 5k/ngày. |
| **`ReportService`** | `ReportServiceImpl` | `@Service`<br>`@Transactional(readOnly)` | `BorrowingRepository`<br>`BookRepository` | Thống kê phiếu quá hạn, top 5 sách mượn nhiều nhất, tổng hợp lượt mượn theo thành viên. |

---

## 6. Ánh Xạ Luồng Tương Tác Tận Cùng (End-to-End Execution Map)

Bảng tra cứu chi tiết luồng xử lý từ Thao tác UI $ightarrow$ Route $ightarrow$ Method $ightarrow$ DB Query:

| Thao Tác Người Dùng | HTTP Method & Route | Controller Method | Service Method | Repository / Query Executed | View / Redirect Destination |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Tìm kiếm & Phân trang sách** | `GET /books?keyword=java&page=0` | `BookController.listBooks()` | `BookService.findAll(spec, pageable)` | `SELECT b FROM Book b WHERE ... LIMIT 10 OFFSET 0` | `books/list.html` |
| **Thêm mới Sách** | `POST /books` | `BookController.createBook()` | `BookService.createBook(BookForm)` | `INSERT INTO books (isbn, title, total_quantity, available_quantity...)` | `redirect:/books` |
| **Tạo Phiếu Mượn** | `POST /borrowings` | `BorrowingController.create()` | `BorrowingService.createBorrowing()` | `UPDATE books SET available_quantity = available_quantity - ? WHERE id = ?`<br>`INSERT INTO borrowings ...` | `redirect:/borrowings/{id}` |
| **Trả sách (Từng phần)** | `POST /borrowings/{id}/return` | `BorrowingController.processReturn()` | `BorrowingService.processReturn()` | `UPDATE borrowing_details SET returned_quantity = ? ...`<br>`UPDATE books SET available_quantity = available_quantity + ?` | `redirect:/borrowings/{id}` |
| **Xem báo cáo quá hạn** | `GET /reports/overdue` | `ReportController.overdueReport()` | `ReportService.getOverdueReport()` | `SELECT b FROM Borrowing b WHERE b.dueDate < CURRENT_DATE AND b.status != 'RETURNED'` | `reports/overdue.html` |

---

> **Ghi chú bảo trì**: Tài liệu này cần được cập nhật song song khi có thay đổi về cấu trúc package, tên API endpoint hoặc logic nghiệp vụ cốt lõi của hệ thống.
