package com.api.auth.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequest {
    private String userId;
    private String phoneNumber;
    private String newPassword;
}
