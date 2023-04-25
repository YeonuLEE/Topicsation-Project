package com.multicampus.topicsation.security;

import com.multicampus.topicsation.repository.IMemberDAO;
import com.multicampus.topicsation.repository.ISignUpDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private ISignUpDAO dao;

    public CustomUserDetailsService(){
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //스프링시큐리티는 내부적으로  UserDetails 타입의 객체를 이용하여 패스워드 검사, 사용자 권한 검사를 확인
    // DB로부터 회원정보를 가져와 회원인지 아닌지 체크여부를 함
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
       log.info("loadUserByUsername: " +  email);

        int count = dao.checkEmailDAO(email);
        if(count == 0) {
            throw new UsernameNotFoundException(email);
        }

        UserDetails userDetails = User.builder().username("user1")
                .password(passwordEncoder.encode("1111"))
                .authorities("ROLE_USER")
                .build();
        return userDetails;
    }
}
