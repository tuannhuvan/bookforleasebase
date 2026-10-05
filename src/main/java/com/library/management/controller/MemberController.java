package com.library.management.controller;

import com.library.management.dto.request.MemberForm;
import com.library.management.enums.MemberStatus;
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
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String listMembers(@RequestParam(value = "status", required = false) MemberStatus status,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var memberPage = memberService.findAll(status, pageable);

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("status", status);
        model.addAttribute("statuses", MemberStatus.values());
        return "members/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("isEdit", false);
        return "members/form";
    }

    @PostMapping
    public String createMember(@Valid @ModelAttribute("memberForm") MemberForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", MemberStatus.values());
            model.addAttribute("isEdit", false);
            return "members/form";
        }
        memberService.createMember(form);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm mới thành viên thành công!");
        return "redirect:/members";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        var member = memberService.findById(id);
        var form = MemberForm.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .status(member.getStatus())
                .build();

        model.addAttribute("memberForm", form);
        model.addAttribute("memberId", id);
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("isEdit", true);
        return "members/form";
    }

    @PostMapping("/{id}")
    public String updateMember(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("memberForm") MemberForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberId", id);
            model.addAttribute("statuses", MemberStatus.values());
            model.addAttribute("isEdit", true);
            return "members/form";
        }
        memberService.updateMember(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành viên thành công!");
        return "redirect:/members";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        var member = memberService.toggleStatus(id);
        String msg = member.getStatus() == MemberStatus.BLOCKED ?
                "Đã khóa tài khoản thành viên " + member.getFullName() :
                "Đã mở khóa tài khoản thành viên " + member.getFullName();
        redirectAttributes.addFlashAttribute("successMessage", msg);
        return "redirect:/members";
    }
}
