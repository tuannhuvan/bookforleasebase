package com.library.management.service;

import com.library.management.dto.request.BorrowingRequest;
import com.library.management.dto.request.ReturnRequest;
import com.library.management.dto.response.BorrowingDTO;
import com.library.management.enums.BorrowingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowingService {
    Page<BorrowingDTO> findAll(String keyword, BorrowingStatus status, Pageable pageable);
    BorrowingDTO findById(Long id);
    BorrowingDTO createBorrowing(BorrowingRequest request);
    BorrowingDTO processReturn(Long borrowingId, ReturnRequest request);
}
