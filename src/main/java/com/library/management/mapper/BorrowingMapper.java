package com.library.management.mapper;

import com.library.management.dto.response.BorrowingDTO;
import com.library.management.dto.response.BorrowingDetailDTO;
import com.library.management.entity.Borrowing;
import com.library.management.entity.BorrowingDetail;
import com.library.management.enums.BorrowingStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class BorrowingMapper {

    public static BorrowingDTO toDTO(Borrowing borrowing) {
        if (borrowing == null) return null;

        long overdueDays = 0;
        if (borrowing.getStatus() != BorrowingStatus.RETURNED && LocalDate.now().isAfter(borrowing.getDueDate())) {
            overdueDays = ChronoUnit.DAYS.between(borrowing.getDueDate(), LocalDate.now());
        }

        var details = borrowing.getDetails() != null ?
                borrowing.getDetails().stream().map(BorrowingMapper::toDetailDTO).collect(Collectors.toList()) :
                java.util.Collections.<BorrowingDetailDTO>emptyList();

        return BorrowingDTO.builder()
                .id(borrowing.getId())
                .memberId(borrowing.getMember() != null ? borrowing.getMember().getId() : null)
                .memberName(borrowing.getMember() != null ? borrowing.getMember().getFullName() : null)
                .memberEmail(borrowing.getMember() != null ? borrowing.getMember().getEmail() : null)
                .borrowDate(borrowing.getBorrowDate())
                .dueDate(borrowing.getDueDate())
                .returnedDate(borrowing.getReturnedDate())
                .status(borrowing.getStatus())
                .totalFine(borrowing.getTotalFine())
                .overdueDays(overdueDays)
                .details(details)
                .build();
    }

    public static BorrowingDetailDTO toDetailDTO(BorrowingDetail detail) {
        if (detail == null) return null;
        return BorrowingDetailDTO.builder()
                .id(detail.getId())
                .bookId(detail.getBook() != null ? detail.getBook().getId() : null)
                .bookTitle(detail.getBook() != null ? detail.getBook().getTitle() : null)
                .bookIsbn(detail.getBook() != null ? detail.getBook().getIsbn() : null)
                .quantity(detail.getQuantity())
                .returnedQuantity(detail.getReturnedQuantity())
                .fineAmount(detail.getFineAmount())
                .build();
    }
}
