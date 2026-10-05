package com.library.management.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Resource Not Found");
        model.addAttribute("message", ex.getMessage());
        return "error/error";
    }

    @ExceptionHandler({BusinessRuleException.class, OutOfStockException.class})
    public String handleBusinessException(RuntimeException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("error", "Yêu cầu không hợp lệ / Vi phạm quy tắc nghiệp vụ");
        model.addAttribute("message", ex.getMessage());
        return "error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("error", "Lỗi hệ thống");
        model.addAttribute("message", ex.getMessage());
        return "error/error";
    }
}
