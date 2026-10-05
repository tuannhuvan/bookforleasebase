package com.library.management.mapper;

import com.library.management.dto.request.MemberForm;
import com.library.management.dto.response.MemberDTO;
import com.library.management.entity.Member;

public class MemberMapper {

    public static MemberDTO toDTO(Member member) {
        if (member == null) return null;
        return MemberDTO.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .status(member.getStatus())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberForm toForm(Member member) {
        if (member == null) return null;
        return MemberForm.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .status(member.getStatus())
                .build();
    }
}
