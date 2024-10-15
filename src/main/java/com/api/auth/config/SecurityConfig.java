package com.api.auth.config;

import com.api.auth.jwt.JwtRequestFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return (PasswordEncoder)new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.requestMatchers(new String[] { "/api/user/**", "/api/send-verification-code", "/api/verify-code" })).permitAll().anyRequest()).authenticated()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore((Filter)this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
