package com.multicampus.topicsation.service;


import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.repository.ILoginDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberManageService implements IMemberManageService{

    @Autowired
    private final ILoginDAO dao;

    public LoginDTO login(Map<String, String> map) {
        String email = map.get("email");
        String password = map.get("password");
        Map<String, String> result = new HashMap<>();
        result.put("email",email);
        result.put("password",password);
        return dao.login(result);
    }
}