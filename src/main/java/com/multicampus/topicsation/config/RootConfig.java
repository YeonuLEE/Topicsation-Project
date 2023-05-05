package com.multicampus.topicsation.config;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public BCrypt encryConfig() {
        return new BCrypt();
    }
}
