package com.library.management.service;

import com.library.management.dto.response.MemberStatsReportDTO;
import com.library.management.dto.response.OverdueReportDTO;
import com.library.management.dto.response.TopBookReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    Page<OverdueReportDTO> getOverdueReport(Pageable pageable);
    List<TopBookReportDTO> getTopBorrowedBooks(int limit);
    List<MemberStatsReportDTO> getMemberBorrowStats();
}
