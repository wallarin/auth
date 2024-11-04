package com.api.auth.DTO;

import lombok.Data;

@Data
public class InquiryRequest {
    private String userId;
    private String content;
}
