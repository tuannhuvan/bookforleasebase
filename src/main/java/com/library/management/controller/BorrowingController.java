package com.library.management.controller;

import com.library.management.dto.request.BorrowingRequest;
import com.library.management.dto.request.ReturnRequest;
import com.library.management.enums.BookStatus;
import com.library.management.enums.BorrowingStatus;
import com.library.management.enums.MemberStatus;
import com.library.management.service.BookService;
import com.library.management.service.BorrowingService;
import com.library.management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    private final MemberService memberService;
    private final BookService bookService;

    public BorrowingController(BorrowingService borrowingService, MemberService memberService, BookService bookService) {
        this.borrowingService = borrowingService;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @GetMapping
    public String listBorrowings(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "status", required = false) BorrowingStatus status,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var borrowingPage = borrowingService.findAll(keyword, status, pageable);

        model.addAttribute("borrowingPage", borrowingPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("statuses", BorrowingStatus.values());
        return "borrowings/list";
    }

    @GetMapping("/{id}")
    public String viewBorrowingDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("borrowing", borrowingService.findById(id));
        return "borrowings/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        var borrowingRequest = new BorrowingRequest();
        borrowingRequest.getItems().add(new BorrowingRequest.BorrowingItemRequest());

        model.addAttribute("borrowingRequest", borrowingRequest);
        model.addAttribute("members", memberService.findAll(MemberStatus.ACTIVE, PageRequest.of(0, 100)).getContent());
        model.addAttribute("books", bookService.findAll(null, null, BookStatus.AVAILABLE, PageRequest.of(0, 200)).getContent());
        return "borrowings/create";
    }

    @PostMapping
    public String createBorrowing(@Valid @ModelAttribute("borrowingRequest") BorrowingRequest request,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("members", memberService.findAll(MemberStatus.ACTIVE, PageRequest.of(0, 100)).getContent());
            model.addAttribute("books", bookService.findAll(null, null, BookStatus.AVAILABLE, PageRequest.of(0, 200)).getContent());
            return "borrowings/create";
        }

        var saved = borrowingService.createBorrowing(request);
        redirectAttributes.addFlashAttribute("successMessage", "Lập phiếu mượn sách #" + saved.getId() + " thành công!");
        return "redirect:/borrowings/" + saved.getId();
    }

    @GetMapping("/{id}/return")
    public String showReturnForm(@PathVariable("id") Long id, Model model) {
        var borrowing = borrowingService.findById(id);

        var returnRequest = new ReturnRequest();
        returnRequest.setBorrowingId(id);

        for (var detail : borrowing.getDetails()) {
            int remaining = detail.getQuantity() - detail.getReturnedQuantity();
            var item = ReturnRequest.ReturnItemRequest.builder()
                    .detailId(detail.getId())
                    .returnQuantity(remaining)
                    .build();
            returnRequest.getItems().add(item);
        }

        model.addAttribute("borrowing", borrowing);
        model.addAttribute("returnRequest", returnRequest);
        return "borrowings/return";
    }

    @PostMapping("/{id}/return")
    public String processReturn(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("returnRequest") ReturnRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("borrowing", borrowingService.findById(id));
            return "borrowings/return";
        }

        var updated = borrowingService.processReturn(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Xử lý trả sách cho phiếu #" + id + " thành công!");
        return "redirect:/borrowings/" + updated.getId();
    }
}
