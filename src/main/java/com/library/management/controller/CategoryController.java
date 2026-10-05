package com.library.management.controller;

import com.library.management.dto.request.CategoryForm;
import com.library.management.dto.response.CategoryDTO;
import com.library.management.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        model.addAttribute("isEdit", false);
        return "categories/form";
    }

    @PostMapping
    public String createCategory(@Valid @ModelAttribute("categoryForm") CategoryForm form,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "categories/form";
        }
        categoryService.createCategory(form);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo thể loại mới thành công!");
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        CategoryDTO category = categoryService.findById(id);
        CategoryForm form = CategoryForm.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
        model.addAttribute("categoryForm", form);
        model.addAttribute("categoryId", id);
        model.addAttribute("isEdit", true);
        return "categories/form";
    }

    @PostMapping("/{id}")
    public String updateCategory(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("categoryForm") CategoryForm form,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryId", id);
            model.addAttribute("isEdit", true);
            return "categories/form";
        }
        categoryService.updateCategory(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thể loại thành công!");
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa mềm thể loại!");
        return "redirect:/categories";
    }
}
