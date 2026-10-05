package com.library.management.controller;

import com.library.management.dto.request.BookForm;
import com.library.management.enums.BookStatus;
import com.library.management.service.BookService;
import com.library.management.service.CategoryService;
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
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    public BookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listBooks(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "categoryId", required = false) Long categoryId,
                            @RequestParam(value = "status", required = false) BookStatus status,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var bookPage = bookService.findAll(keyword, categoryId, status, pageable);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("status", status);
        model.addAttribute("categories", categoryService.findActiveCategories());
        model.addAttribute("statuses", BookStatus.values());
        return "books/list";
    }

    @GetMapping("/{id}")
    public String viewBookDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "books/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        model.addAttribute("categories", categoryService.findActiveCategories());
        model.addAttribute("statuses", BookStatus.values());
        model.addAttribute("isEdit", false);
        return "books/form";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute("bookForm") BookForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findActiveCategories());
            model.addAttribute("statuses", BookStatus.values());
            model.addAttribute("isEdit", false);
            return "books/form";
        }

        bookService.createBook(form);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sách mới thành công!");
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        var book = bookService.findById(id);
        var form = BookForm.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalQuantity(book.getTotalQuantity())
                .categoryId(book.getCategoryId())
                .status(book.getStatus())
                .build();

        model.addAttribute("bookForm", form);
        model.addAttribute("bookId", id);
        model.addAttribute("categories", categoryService.findActiveCategories());
        model.addAttribute("statuses", BookStatus.values());
        model.addAttribute("isEdit", true);
        return "books/form";
    }

    @PostMapping("/{id}")
    public String updateBook(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("bookForm") BookForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", id);
            model.addAttribute("categories", categoryService.findActiveCategories());
            model.addAttribute("statuses", BookStatus.values());
            model.addAttribute("isEdit", true);
            return "books/form";
        }

        bookService.updateBook(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin sách thành công!");
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sách thành công!");
        return "redirect:/books";
    }
}
