package com.api.auth.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Users")
public class User {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Id
    @Column(name = "phone_number")
    private String phone;

    @Column(name = "is_verified")
    private String isVerified;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
}
