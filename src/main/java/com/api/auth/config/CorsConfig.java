package com.api.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 설정 파일에서 IP 주소 읽기
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/cors-config.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        config.addAllowedOrigin(properties.getProperty("allowed.origin1"));
        config.addAllowedOrigin(properties.getProperty("allowed.origin2"));
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
