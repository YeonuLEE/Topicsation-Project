package com.multicampus.topicsation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://www.topicsation.online") // Node.js 서버 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 요청 허용 메소드
                .allowedHeaders("*")
                .allowCredentials(true); // 요청 허용 헤더
    }
}
