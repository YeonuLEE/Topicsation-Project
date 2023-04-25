package com.multicampus.topicsation.service.security;

import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.repository.ILoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private ILoginDAO dao;

    public CustomUserDetailsService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //스프링시큐리티는 내부적으로  UserDetails 타입의 객체를 이용하여 패스워드 검사, 사용자 권한 검사를 확인
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("email", username);
        System.out.println(paramMap.values());

        LoginDTO dto = dao.login(paramMap);
        System.out.println("customServie getrole: "+dto.getRole());
        System.out.println("customServie getemail: "+dto.getEmail());
        System.out.println("customServie getpwd: "+dto.getPassword());
        System.out.println("UserDetailsService");
        if (dto == null) {
            throw new UsernameNotFoundException("일치하는 회원이 없습니다.");
        }
        return buildUserForAuthentication(dto, getAuthorities(dto.getRole()));
    }

    private UserDetails buildUserForAuthentication(LoginDTO dto, List<GrantedAuthority> authorities) {
        System.out.println(dto.getEmail()+", "+dto.getPassword());
        System.out.println(authorities.size());
        return new User(dto.getEmail(), dto.getPassword(), authorities);
    }

    private List<GrantedAuthority> getAuthorities(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(username));
        return authorities;
    }
}
