package com.library.management.controller;

import com.library.management.service.BookService;
import com.library.management.service.BorrowingService;
import com.library.management.service.CategoryService;
import com.library.management.service.MemberService;
import com.library.management.service.ReportService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final BookService bookService;
    private final MemberService memberService;
    private final BorrowingService borrowingService;
    private final ReportService reportService;

    public HomeController(CategoryService categoryService, BookService bookService, MemberService memberService, BorrowingService borrowingService, ReportService reportService) {
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.memberService = memberService;
        this.borrowingService = borrowingService;
        this.reportService = reportService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalCategories", categoryService.findAll().size());
        model.addAttribute("totalBooks", bookService.findAll(null, null, null, PageRequest.of(0, 1)).getTotalElements());
        model.addAttribute("totalMembers", memberService.findAll(null, PageRequest.of(0, 1)).getTotalElements());
        model.addAttribute("totalBorrowings", borrowingService.findAll(null, null, PageRequest.of(0, 1)).getTotalElements());

        var overduePage = reportService.getOverdueReport(PageRequest.of(0, 5));
        model.addAttribute("recentOverdue", overduePage.getContent());
        model.addAttribute("topBooks", reportService.getTopBorrowedBooks(5));

        return "home";
    }
}
