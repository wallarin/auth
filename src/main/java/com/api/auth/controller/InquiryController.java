package com.api.auth.controller;

import com.api.auth.DTO.Inquiry;
import com.api.auth.DTO.InquiryRequest;
import com.api.auth.DTO.InquiryResponseDto;
import com.api.auth.service.InquiryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {
    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/submit")
    public Inquiry submitInquiry(@RequestBody InquiryRequest request) {
        return inquiryService.saveInquiry(request.getUserId(), request.getContent());
    }

    @GetMapping("/myInquiries")
    public List<InquiryResponseDto> getMyInquiries(@RequestParam("userId") String userId) {
        return inquiryService.getInquiriesByUserId(userId);
    }
}
