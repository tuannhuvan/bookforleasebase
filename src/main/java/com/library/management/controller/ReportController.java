package com.library.management.controller;

import com.library.management.service.ReportService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/overdue")
    public String overdueReport(@RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                Model model) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("overduePage", reportService.getOverdueReport(pageable));
        return "reports/overdue";
    }

    @GetMapping("/top-books")
    public String topBooksReport(@RequestParam(value = "limit", defaultValue = "10") int limit, Model model) {
        model.addAttribute("topBooks", reportService.getTopBorrowedBooks(limit));
        model.addAttribute("limit", limit);
        return "reports/top-books";
    }

    @GetMapping("/member-stats")
    public String memberStatsReport(Model model) {
        model.addAttribute("memberStats", reportService.getMemberBorrowStats());
        return "reports/member-stats";
    }
}
