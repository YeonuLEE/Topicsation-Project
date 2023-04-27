package com.multicampus.topicsation.config;

import com.multicampus.topicsation.token.TokenProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //Spring Security 활성화
@EnableMethodSecurity //@PreAuthorize 어노테이션 메소드 단위로 추가하기 위해
public class CustomSecurityConfig {

    private final TokenProvider tokenProvider;

    public CustomSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 토큰을 사용하기 때문에 csrf 설정 disable
                .csrf().disable()

                // 세션 사용하지 않기 때문에 세션 설정 STATELESS
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 토큰이 없는 상태에서 요청이 들어오는 API들은 permitAll
                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/vendor/**", "/assets/**/**").permitAll()
                .antMatchers("/members/**").permitAll()
                .antMatchers("/main/**").permitAll()
                .antMatchers("/lesson/**").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/mypage/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/members/signin")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/signout")
                .logoutSuccessUrl("/main")
                .invalidateHttpSession(true)
                // JwtFilter를 addFilterBefore로 등록했던 jwtSecurityConfig 클래스 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    //비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
