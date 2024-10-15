package com.api.auth.jwt;

import com.api.auth.DTO.User;
import com.api.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), new ArrayList<>());
    }
}
