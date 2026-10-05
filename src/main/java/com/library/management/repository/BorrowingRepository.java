package com.library.management.repository;

import com.library.management.entity.Borrowing;
import com.library.management.enums.BorrowingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long>, JpaSpecificationExecutor<Borrowing> {

    Page<Borrowing> findByStatus(BorrowingStatus status, Pageable pageable);

    @Query("SELECT b FROM Borrowing b WHERE b.dueDate < :today AND b.status <> com.library.management.enums.BorrowingStatus.RETURNED")
    Page<Borrowing> findOverdueBorrowings(@Param("today") LocalDate today, Pageable pageable);

    @Query("SELECT bd.book.id AS bookId, bd.book.title AS title, bd.book.isbn AS isbn, SUM(bd.quantity) AS totalBorrowed " +
           "FROM BorrowingDetail bd GROUP BY bd.book.id, bd.book.title, bd.book.isbn ORDER BY SUM(bd.quantity) DESC")
    List<Object[]> findTopBorrowedBooks(Pageable pageable);

    @Query("SELECT b.member.id AS memberId, b.member.fullName AS fullName, b.member.email AS email, COUNT(b.id) AS totalBorrowings " +
           "FROM Borrowing b GROUP BY b.member.id, b.member.fullName, b.member.email ORDER BY COUNT(b.id) DESC")
    List<Object[]> findMemberBorrowStats();

    @Query("SELECT COUNT(bd) > 0 FROM BorrowingDetail bd WHERE bd.book.id = :bookId AND bd.borrowing.status <> com.library.management.enums.BorrowingStatus.RETURNED AND bd.returnedQuantity < bd.quantity")
    boolean isBookCurrentlyBorrowed(@Param("bookId") Long bookId);
}
