package com.library.management.service;

import com.library.management.dto.request.MemberForm;
import com.library.management.dto.response.MemberDTO;
import com.library.management.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    Page<MemberDTO> findAll(MemberStatus status, Pageable pageable);
    MemberDTO findById(Long id);
    MemberDTO createMember(MemberForm form);
    MemberDTO updateMember(Long id, MemberForm form);
    MemberDTO toggleStatus(Long id);
}
