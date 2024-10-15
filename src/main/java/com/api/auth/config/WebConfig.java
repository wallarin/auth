package com.api.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 모든 경로에 대해 index.html로 리다이렉트
//        registry.addViewController("/{spring:[^\\.]+}")
//                .setViewName("forward:/index.html");
//        registry.addViewController("/**/{spring:[^\\.]+}")
//                .setViewName("forward:/index.html");
    }

}
