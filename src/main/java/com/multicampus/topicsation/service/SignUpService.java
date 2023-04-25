package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.repository.ISignUpDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService implements ISignUpService {
    @Autowired
    private final ISignUpDAO dao;

    private final PasswordEncoder passwordEncoder;

    // 아이디가 중복되는 경우 true룰 반환하며 그렇지 않은 경우 false를 반환
    @Override
    public void checkEmail(String email) {
        int existEmail = dao.checkEmailDAO(email);
        if(existEmail != 0) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원으로 등록되었을 때 true를, 어떠한 이유때문에 등록되지 않은 경우 false를 반환
    @Override
    public int addTutee(SignUpDTO dto) {
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        //인코딩한 비밀번호로 대체
        dto.setPassword(encodedPassword);
        return dao.addTuteeDAO(dto);
    }

    @Override
    public int addTutor(SignUpDTO dto) {
        return dao.addTutorDAO(dto);
    }
}
