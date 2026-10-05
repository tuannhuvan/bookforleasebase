-- V1__init_schema.sql
-- Create categories table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create books table
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    total_quantity INT NOT NULL CHECK (total_quantity >= 0),
    available_quantity INT NOT NULL CHECK (available_quantity >= 0),
    status VARCHAR(20) NOT NULL,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_books_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create members table
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create borrowings table
CREATE TABLE borrowings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    returned_date DATE,
    status VARCHAR(20) NOT NULL,
    total_fine DECIMAL(12, 2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_borrowings_member FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Create borrowing_details table
CREATE TABLE borrowing_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    borrowing_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    returned_quantity INT DEFAULT 0 CHECK (returned_quantity >= 0),
    fine_amount DECIMAL(12, 2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_details_borrowing FOREIGN KEY (borrowing_id) REFERENCES borrowings(id),
    CONSTRAINT fk_details_book FOREIGN KEY (book_id) REFERENCES books(id)
);
