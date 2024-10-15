package com.api.auth.service;

import com.api.auth.DTO.User;
import com.api.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
