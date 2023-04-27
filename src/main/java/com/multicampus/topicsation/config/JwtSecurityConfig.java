package com.multicampus.topicsation.config;

import com.multicampus.topicsation.token.JwtFilter;
import com.multicampus.topicsation.token.TokenProvider;

//TokenProvider, JwtFilter를 SecurityConfig에 적용할 때 사용
//SecurityConfigurerAdapter를 extends
//public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    private final TokenProvider tokenProvider;
//
//    //tokenProvider 주입
//    public JwtSecurityConfig(TokenProvider tokenProvider) {
//        this.tokenProvider = tokenProvider;
//    }
//
//    @Override
//    public void configure(HttpSecurity http) {
//
//        System.out.println("config");
//
//        JwtFilter customFilter = new JwtFilter(tokenProvider);
//        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
