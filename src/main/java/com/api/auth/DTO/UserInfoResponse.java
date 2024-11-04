package com.api.auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data  // Getter, Setter, toString, equals, hashCode 자동 생성
@AllArgsConstructor  // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성
public class UserInfoResponse {
    private String userId;
    private String nickname;
    private String phone;
    private String gender;
    private LocalDate birthdate;
}
