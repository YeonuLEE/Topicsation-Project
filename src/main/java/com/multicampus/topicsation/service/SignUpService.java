package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.repository.ISignUpDAO;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignUpService implements ISignUpService {
    @Autowired
    private final ISignUpDAO dao;

//    @Autowired
//    private final PasswordEncoder passwordEncoder;

    // 아이디가 중복되는 경우 true룰 반환하며 그렇지 않은 경우 false를 반환
    @Override
    public boolean signUpProcess(SignUpDTO signUpDTO) {

        int existEmail = dao.checkEmailDAO(signUpDTO.getEmail());
        if(existEmail != 0) {
            return false;
        }else{
            System.out.println("신규 회원입니다.");
            if(signUpDTO.getRole().equals("tutee")){
                dao.addTuteeDAO(signUpDTO);
            } else {
                dao.addTutorDAO(signUpDTO);
            }
            return true;
        }

    }

}
