package com.api.auth.DTO;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String nickname;
    private String password;
    private String phone;
    private String birth;
    private String gender;
}
