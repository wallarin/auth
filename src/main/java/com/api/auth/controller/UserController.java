package com.api.auth.controller;

import com.api.auth.DTO.User;
import com.api.auth.jwt.JwtUtil;
import com.api.auth.service.UserService;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"api/user"})
public class UserController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Generated
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/signup"})
    public User signUp(@RequestBody User user) {
        System.out.println(user.getUserId());
        System.out.println(user.getPassword());
        System.out.println(user.getNickname());
        System.out.println(user.getPhone());
        System.out.println(user.getIsVerified());
        System.out.println(user.getGender());
        System.out.println("" + user.getBirthdate());
        return this.userService.saveUser(user);
    }

    @PostMapping({"/check-duplication"})
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestBody Map<String, String> request) {
        String type = request.get("type");
        String value = request.get("value");
        boolean isDuplicate = false;
        if ("userId".equals(type)) {
            isDuplicate = this.userService.isUserIdDuplicate(value);
        } else if ("nickname".equals(type)) {
            isDuplicate = this.userService.isNicknameDuplicate(value);
        } else if ("phoneNumber".equals(type)) {
            isDuplicate = this.userService.isPhoneDuplicate(value);
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", Boolean.valueOf(isDuplicate));
        return ResponseEntity.ok(response);
    }

    @PostMapping({"/login"})
    public String createAuthenticationToken(@RequestBody User authRequest) throws Exception {
        this.authenticationManager.authenticate((Authentication)new UsernamePasswordAuthenticationToken(authRequest
                .getUserId(), authRequest.getPassword()));
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authRequest.getUserId());
        return this.jwtUtil.generateToken(userDetails);
    }
}
