package com.api.auth.service;

import com.api.auth.DTO.Inquiry;
import com.api.auth.DTO.InquiryResponse;
import com.api.auth.DTO.InquiryResponseDto;
import com.api.auth.repository.InquiryRepository;
import com.api.auth.repository.InquiryResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryResponseRepository responseRepository;

    public InquiryService(InquiryRepository inquiryRepository, InquiryResponseRepository responseRepository) {
        this.inquiryRepository = inquiryRepository;
        this.responseRepository = responseRepository;
    }

    @Transactional
    public Inquiry saveInquiry(String userId, String content) {
        Inquiry inquiry = new Inquiry();
        inquiry.setUserId(userId);
        inquiry.setContent(content);
        return inquiryRepository.save(inquiry);
    }

    public List<InquiryResponseDto> getInquiriesByUserId(String userId) {
        List<Inquiry> inquiries = inquiryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<InquiryResponseDto> responseDtos = new ArrayList<>();

        for (Inquiry inquiry : inquiries) {
            InquiryResponseDto dto = new InquiryResponseDto(inquiry);

            // 답변이 있는 경우 찾아서 DTO에 추가
            List<InquiryResponse> responses = responseRepository.findByInquiryId(inquiry.getId());
            if (!responses.isEmpty()) {
                dto.setResponse(responses.get(0)); // 첫 번째 응답을 설정
            }

            responseDtos.add(dto);
        }

        return responseDtos;
    }
}
