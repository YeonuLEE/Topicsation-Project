package com.multicampus.topicsation.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
@Configuration // 해당 클래스가 springsecurity와 관련된 설정 클래스임을 나타냄
@EnableWebSecurity // SpringSecurityFilterChain이 자동으로 포함됨(WebSecurityConfigurerAdapter 상속시)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //각 유저의 권한은 CustomUserDetailsService를 구현한 클래스에서 가져옴
//    @Autowired
//    private CustomUserDetailsService userDetailsService; // 사용할 유저 정보

    // BCryptPasswordEncoder의 해시함수를 이용하여 비밀번호 암호화처리
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 실제 인증을 진행할 provider
    // DB로부터 아이디, 비번이 맞는지 해당 유저가 어떤 권한을 가지는지 체크(로그인할때 필요한 정보를 가져옴)
    // userDetailsService인터페이스를 상속받은 클래스가 있다면 그 클래스에서 인증을 시도하면됨
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }

    // 해당자원에대한 인증을 무시할 경로들 해제
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**", "/assets/image/**", "/vendor/**");
    }
    //http 요청에 대한 보안을 설정함
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 일반 사용자에 대해 Session을 저장하지 않으므로 csrf를 disable 처리함
        http.csrf().disable();
        // 요청에 대한 인가를 설정정
        http.authorizeRequests()
//                .antMatchers("/members/").access("hasRole('ROLE_TUTEE')") // 특정권한이 있는 사람만 접근 가능
//                .antMatchers("/members").hasRole("TUTEE")
//                .antMatchers("/members/").access("hasRole('ROLE_TUTOR')")
//                .antMatchers("/memberts/").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/main", "/").authenticated() //
                .anyRequest().permitAll(); // 나머지 요청은 인증된 사용자만 접근할 수 있도록 설정

        http.formLogin()// formLogin 인증방식을 사용함
            .loginPage("/members/signin")
                .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트트주소
                .invalidateHttpSession(true) //로그아웃 후 세션 전체 삭제 여부
                .permitAll();
    }
}
