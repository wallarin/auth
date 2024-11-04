package com.api.auth.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InquiryResponseDto {
    private Long inquiryId;
    private String userId;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String responseContent; // 답변 내용
    private String adminId;         // 답변 작성자 ID
    private LocalDateTime responseCreatedAt; // 답변 작성 시간

    public InquiryResponseDto(Inquiry inquiry) {
        this.inquiryId = inquiry.getId();
        this.userId = inquiry.getUserId();
        this.content = inquiry.getContent();
        this.status = inquiry.getStatus();
        this.createdAt = inquiry.getCreatedAt();
        this.updatedAt = inquiry.getUpdatedAt();
    }

    public void setResponse(InquiryResponse response) {
        this.responseContent = response.getResponse();
        this.adminId = response.getAdminId();
        this.responseCreatedAt = response.getCreatedAt();
    }
}
