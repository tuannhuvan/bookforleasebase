package com.library.management.service.impl;

import com.library.management.dto.request.MemberForm;
import com.library.management.dto.response.MemberDTO;
import com.library.management.entity.Member;
import com.library.management.enums.MemberStatus;
import com.library.management.exception.BusinessRuleException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.mapper.MemberMapper;
import com.library.management.repository.MemberRepository;
import com.library.management.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Page<MemberDTO> findAll(MemberStatus status, Pageable pageable) {
        if (status != null) {
            return memberRepository.findByStatus(status, pageable).map(MemberMapper::toDTO);
        }
        return memberRepository.findAll(pageable).map(MemberMapper::toDTO);
    }

    @Override
    public MemberDTO findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành viên với ID: " + id));
        return MemberMapper.toDTO(member);
    }

    @Override
    @Transactional
    public MemberDTO createMember(MemberForm form) {
        if (memberRepository.existsByEmail(form.getEmail().trim().toLowerCase())) {
            throw new BusinessRuleException("Email '" + form.getEmail() + "' đã tồn tại trong hệ thống!");
        }

        Member member = Member.builder()
                .fullName(form.getFullName().trim())
                .email(form.getEmail().trim().toLowerCase())
                .phone(form.getPhone() != null ? form.getPhone().trim() : null)
                .status(form.getStatus() != null ? form.getStatus() : MemberStatus.ACTIVE)
                .build();

        Member saved = memberRepository.save(member);
        return MemberMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public MemberDTO updateMember(Long id, MemberForm form) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành viên với ID: " + id));

        if (memberRepository.existsByEmailAndIdNot(form.getEmail().trim().toLowerCase(), id)) {
            throw new BusinessRuleException("Email '" + form.getEmail() + "' đã thuộc về thành viên khác!");
        }

        member.setFullName(form.getFullName().trim());
        member.setEmail(form.getEmail().trim().toLowerCase());
        member.setPhone(form.getPhone() != null ? form.getPhone().trim() : null);
        if (form.getStatus() != null) {
            member.setStatus(form.getStatus());
        }

        Member updated = memberRepository.save(member);
        return MemberMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public MemberDTO toggleStatus(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành viên với ID: " + id));

        if (member.getStatus() == MemberStatus.ACTIVE) {
            member.setStatus(MemberStatus.BLOCKED);
        } else {
            member.setStatus(MemberStatus.ACTIVE);
        }

        Member updated = memberRepository.save(member);
        return MemberMapper.toDTO(updated);
    }
}
