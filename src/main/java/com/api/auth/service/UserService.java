package com.api.auth.service;

import com.api.auth.DTO.User;
import com.api.auth.DTO.UserInfoResponse;
import com.api.auth.DTO.UserUpdateRequest;
import com.api.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Generated
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User saveUser(User user) {
        String encryptedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return (User)this.userRepository.save(user);
    }

    public boolean isUserIdDuplicate(String userId) {
        return this.userRepository.existsByUserId(userId);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return this.userRepository.existsByNickname(nickname);
    }

    public boolean isPhoneDuplicate(String phone) {
        return this.userRepository.existsByPhone(phone);
    }

    public Optional<UserInfoResponse> getUserInfo(String userId) {
        return userRepository.findUserInfoByUserId(userId);
    }

    public void updateUser(String userId, UserUpdateRequest updateRequest) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        User user;
        try {
            user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            e.printStackTrace();  // 스택 트레이스 출력
            throw e;  // 예외를 다시 던져서 호출자에게 전달
        }

        System.out.println("test1");

        // 닉네임 업데이트
        if (updateRequest.getNickname() != null) {
            user.setNickname(updateRequest.getNickname());
        }

        System.out.println("test2");

        // 비밀번호 업데이트 (있는 경우에만)
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updateRequest.getPassword());
            user.setPassword(encodedPassword);
        }

        System.out.println("test3");

        // 생년월일 업데이트
        if (updateRequest.getBirth() != null) {
            user.setBirthdate(LocalDate.parse(updateRequest.getBirth()));
        }

        System.out.println("test4");

        // 성별 업데이트
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
        }

        System.out.println("test5");

        userRepository.save(user);
    }

}
