package com.multicampus.topicsation.service;


import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.repository.ILoginDAO;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberManageService implements IMemberManageService{

    @Autowired
    private final ILoginDAO dao;

    @Autowired
    private final BCrypt bCrypt;


    public LoginDTO login(Map<String, String> map) throws Exception {
        String email = map.get("email");
        String password = map.get("password");
        System.out.println(password);

        // email과 password가 null일 경우 예외를 던집니다.
        if (email == null || password == null) {
            throw new IllegalArgumentException("이메일과 패스워드를 입력하세요.");
        }

        Map<String, String> result = new HashMap<>();
        result.put("email",email);

        LoginDTO dto = dao.login(result);

//        if (dto == null || !BCrypt.checkpw(password, dto.getPassword())) {
//            throw new RuntimeException("일치하는 회원이 없습니다.");
//        }

        return dto;
    }
}