package com.multicampus.topicsation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

//        registry.addMapping("/generateToken").allowedOrigins("*");
//        registry.addMapping("/refreshAccessToken").allowedOrigins("*");
//
//        registry.addMapping("/api/**").allowedOrigins("*");
    }
}