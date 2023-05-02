package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.dto.SignUpDTO;

import java.util.Map;

public interface ISignUpService {
    boolean signUpProcess(SignUpDTO signUpDTO);
    boolean sendMail(MailDTO mailDTO);
    void successEmailAuth(SignUpDTO signUpDTO);
    String getFileExtension(String fileName);
}
