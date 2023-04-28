package com.multicampus.topicsation.config;

import com.multicampus.topicsation.token.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public JwtInterceptor jwtInterceptor() {
        JwtInterceptor jwtInterceptor = new JwtInterceptor();
        return jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/members/**")
                .excludePathPatterns("/main","/main/search-all","/main/tutors/{tutor_id}")
                .excludePathPatterns("/mypage/admin","/mypage/{user_id}","/mypage/{user_id}/schedule", "/mypage/{user_id}/history")
                .excludePathPatterns("/lesson/{lesson_id}","/lesson/{lesson_id}/evaluate")
                .excludePathPatterns("/error/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
