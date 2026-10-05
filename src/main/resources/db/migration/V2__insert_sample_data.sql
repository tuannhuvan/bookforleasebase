-- V2__insert_sample_data.sql

-- Insert Categories (5 categories)
INSERT INTO categories (id, name, description, active, created_at) VALUES
(1, 'Lập Trình & Công Nghệ', 'Sách về ngôn ngữ lập trình, kiến trúc phần mềm và công nghệ thông tin', TRUE, CURRENT_TIMESTAMP),
(2, 'Kinh Tế & Quản Trị', 'Sách kinh doanh, quản trị doanh nghiệp, tài chính và khởi nghiệp', TRUE, CURRENT_TIMESTAMP),
(3, 'Văn Học & Tiểu Thuyết', 'Các tác phẩm văn học kinh điển và hiện đại trong và ngoài nước', TRUE, CURRENT_TIMESTAMP),
(4, 'Kỹ Năng Sống', 'Sách phát triển bản thân, tư duy tích cực và quản lý thời gian', TRUE, CURRENT_TIMESTAMP),
(5, 'Khoa Học & Lịch Sử', 'Khám phá thế giới tự nhiên, vũ trụ và các mốc lịch sử loài người', TRUE, CURRENT_TIMESTAMP);

-- Insert Books (20 books)
INSERT INTO books (id, category_id, isbn, title, author, total_quantity, available_quantity, status, version, created_at) VALUES
(1, 1, '978-0134685991', 'Effective Java (3rd Edition)', 'Joshua Bloch', 10, 8, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(2, 1, '978-0132350884', 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', 8, 5, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(3, 1, '978-0134494166', 'Clean Architecture', 'Robert C. Martin', 6, 4, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(4, 1, '978-0201633610', 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma, Richard Helm', 5, 5, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(5, 1, '978-1617294945', 'Spring in Action (6th Edition)', 'Craig Walls', 12, 10, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(6, 2, '978-0062457714', 'Nhà Quản Trị Thành Công', 'Peter F. Drucker', 7, 7, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(7, 2, '978-0735211292', 'Atomic Habits (Thói Quen Nguyên Tử)', 'James Clear', 15, 12, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(8, 2, '978-0307465351', 'Zero to One: Notes on Startups', 'Peter Thiel', 10, 10, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(9, 2, '978-0061122415', 'Rich Dad Poor Dad (Dạy Con Làm Giàu)', 'Robert T. Kiyosaki', 20, 18, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(10, 3, '978-0061120084', 'To Kill a Mockingbird (Giết Con Chim Nhại)', 'Harper Lee', 5, 4, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(11, 3, '978-0451524935', '1984', 'George Orwell', 8, 8, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(12, 3, '978-0141439518', 'Pride and Prejudice (Kiêu Hãnh Và Định Kiến)', 'Jane Austen', 6, 6, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(13, 3, '978-0679783268', 'Tội Lỗi Và Hình Phạt', 'Fyodor Dostoevsky', 4, 3, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(14, 4, '978-0671027032', 'Đắc Nhân Tâm (How to Win Friends)', 'Dale Carnegie', 25, 22, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(15, 4, '978-1501124020', 'Tuần Làm Việc 4 Giờ', 'Timothy Ferriss', 9, 9, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(16, 4, '978-0307352156', 'Tư Duy Nhanh Và Chậm', 'Daniel Kahneman', 7, 7, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(17, 5, '978-0062316097', 'Sapiens: Lược Sử Loài Người', 'Yuval Noah Harari', 10, 8, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(18, 5, '978-0525558613', 'Homo Deus: Lược Sử Tương Lai', 'Yuval Noah Harari', 8, 8, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(19, 5, '978-0345539434', 'Cosmos (Vũ Trụ)', 'Carl Sagan', 6, 6, 'AVAILABLE', 0, CURRENT_TIMESTAMP),
(20, 1, '978-0132354165', 'The Pragmatic Programmer', 'Andrew Hunt, David Thomas', 10, 10, 'AVAILABLE', 0, CURRENT_TIMESTAMP);

-- Insert Members (10 members)
INSERT INTO members (id, full_name, email, phone, status, created_at) VALUES
(1, 'Nguyễn Văn An', 'an.nguyen@example.com', '0901234567', 'ACTIVE', CURRENT_TIMESTAMP),
(2, 'Trần Thị Bình', 'binh.tran@example.com', '0912345678', 'ACTIVE', CURRENT_TIMESTAMP),
(3, 'Lê Hoàng Cường', 'cuong.le@example.com', '0923456789', 'ACTIVE', CURRENT_TIMESTAMP),
(4, 'Phạm Minh Đức', 'duc.pham@example.com', '0934567890', 'BLOCKED', CURRENT_TIMESTAMP),
(5, 'Đỗ Thu Hà', 'ha.do@example.com', '0945678901', 'ACTIVE', CURRENT_TIMESTAMP),
(6, 'Vũ Hùng Dung', 'dung.vu@example.com', '0956789012', 'ACTIVE', CURRENT_TIMESTAMP),
(7, 'Hoàng Khánh Linh', 'linh.hoang@example.com', '0967890123', 'ACTIVE', CURRENT_TIMESTAMP),
(8, 'Bùi Quốc Nam', 'nam.bui@example.com', '0978901234', 'BLOCKED', CURRENT_TIMESTAMP),
(9, 'Đặng Phương Thảo', 'thao.dang@example.com', '0989012345', 'ACTIVE', CURRENT_TIMESTAMP),
(10, 'Ngô Việt Anh', 'anh.ngo@example.com', '0990123456', 'ACTIVE', CURRENT_TIMESTAMP);

-- Insert Borrowings (5 sample borrowings: active, overdue, returned)
-- Borrowing 1: Active borrowing (member 1)
INSERT INTO borrowings (id, member_id, borrow_date, due_date, returned_date, status, total_fine, created_at) VALUES
(1, 1, DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 9, CURRENT_DATE), NULL, 'BORROWED', 0.00, CURRENT_TIMESTAMP);
INSERT INTO borrowing_details (id, borrowing_id, book_id, quantity, returned_quantity, fine_amount, created_at) VALUES
(1, 1, 1, 1, 0, 0.00, CURRENT_TIMESTAMP),
(2, 1, 2, 1, 0, 0.00, CURRENT_TIMESTAMP);

-- Borrowing 2: Overdue borrowing by 10 days (member 2)
INSERT INTO borrowings (id, member_id, borrow_date, due_date, returned_date, status, total_fine, created_at) VALUES
(2, 2, DATEADD('DAY', -24, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), NULL, 'OVERDUE', 0.00, CURRENT_TIMESTAMP);
INSERT INTO borrowing_details (id, borrowing_id, book_id, quantity, returned_quantity, fine_amount, created_at) VALUES
(3, 2, 2, 2, 0, 0.00, CURRENT_TIMESTAMP),
(4, 2, 7, 1, 0, 0.00, CURRENT_TIMESTAMP);

-- Borrowing 3: Returned borrowing (member 3)
INSERT INTO borrowings (id, member_id, borrow_date, due_date, returned_date, status, total_fine, created_at) VALUES
(3, 3, DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -6, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), 'RETURNED', 5000.00, CURRENT_TIMESTAMP);
INSERT INTO borrowing_details (id, borrowing_id, book_id, quantity, returned_quantity, fine_amount, created_at) VALUES
(5, 3, 5, 1, 1, 5000.00, CURRENT_TIMESTAMP);

-- Borrowing 4: Active borrowing (member 5)
INSERT INTO borrowings (id, member_id, borrow_date, due_date, returned_date, status, total_fine, created_at) VALUES
(4, 5, DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 12, CURRENT_DATE), NULL, 'BORROWED', 0.00, CURRENT_TIMESTAMP);
INSERT INTO borrowing_details (id, borrowing_id, book_id, quantity, returned_quantity, fine_amount, created_at) VALUES
(6, 4, 9, 2, 0, 0.00, CURRENT_TIMESTAMP),
(7, 4, 14, 1, 0, 0.00, CURRENT_TIMESTAMP);

-- Borrowing 5: Overdue borrowing by 5 days (member 6)
INSERT INTO borrowings (id, member_id, borrow_date, due_date, returned_date, status, total_fine, created_at) VALUES
(5, 6, DATEADD('DAY', -19, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), NULL, 'OVERDUE', 0.00, CURRENT_TIMESTAMP);
INSERT INTO borrowing_details (id, borrowing_id, book_id, quantity, returned_quantity, fine_amount, created_at) VALUES
(8, 5, 17, 2, 0, 0.00, CURRENT_TIMESTAMP);
