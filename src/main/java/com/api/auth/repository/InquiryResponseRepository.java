package com.api.auth.repository;

import com.api.auth.DTO.InquiryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryResponseRepository extends JpaRepository<InquiryResponse, Long> {
    List<InquiryResponse> findByInquiryId(Long inquiryId);
}
