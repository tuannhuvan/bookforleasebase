package com.library.management.service.impl;

import com.library.management.dto.response.MemberStatsReportDTO;
import com.library.management.dto.response.OverdueReportDTO;
import com.library.management.dto.response.TopBookReportDTO;
import com.library.management.entity.Borrowing;
import com.library.management.repository.BorrowingRepository;
import com.library.management.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private static final long DAILY_FINE_PER_BOOK = 5000L;
    private final BorrowingRepository borrowingRepository;

    public ReportServiceImpl(BorrowingRepository borrowingRepository) {
        this.borrowingRepository = borrowingRepository;
    }

    @Override
    public Page<OverdueReportDTO> getOverdueReport(Pageable pageable) {
        LocalDate today = LocalDate.now();
        Page<Borrowing> overduePage = borrowingRepository.findOverdueBorrowings(today, pageable);

        return overduePage.map(b -> {
            long overdueDays = ChronoUnit.DAYS.between(b.getDueDate(), today);

            int unreturnedBookCount = b.getDetails().stream()
                    .mapToInt(d -> d.getQuantity() - d.getReturnedQuantity())
                    .sum();

            BigDecimal estimatedFine = BigDecimal.valueOf(overdueDays * DAILY_FINE_PER_BOOK * unreturnedBookCount);

            return OverdueReportDTO.builder()
                    .borrowingId(b.getId())
                    .memberName(b.getMember() != null ? b.getMember().getFullName() : null)
                    .memberEmail(b.getMember() != null ? b.getMember().getEmail() : null)
                    .borrowDate(b.getBorrowDate())
                    .dueDate(b.getDueDate())
                    .overdueDays(overdueDays)
                    .estimatedFine(estimatedFine)
                    .build();
        });
    }

    @Override
    public List<TopBookReportDTO> getTopBorrowedBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> rawList = borrowingRepository.findTopBorrowedBooks(pageable);

        return rawList.stream().map(row -> TopBookReportDTO.builder()
                .bookId((Long) row[0])
                .title((String) row[1])
                .isbn((String) row[2])
                .totalBorrowed(((Number) row[3]).longValue())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<MemberStatsReportDTO> getMemberBorrowStats() {
        List<Object[]> rawList = borrowingRepository.findMemberBorrowStats();

        return rawList.stream().map(row -> MemberStatsReportDTO.builder()
                .memberId((Long) row[0])
                .fullName((String) row[1])
                .email((String) row[2])
                .totalBorrowings(((Number) row[3]).longValue())
                .build()
        ).collect(Collectors.toList());
    }
}
