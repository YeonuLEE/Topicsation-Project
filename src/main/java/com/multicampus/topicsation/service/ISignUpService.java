package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SignUpDTO;

public interface ISignUpService {
    void checkEmail(SignUpDTO dto);
    int addTutee(SignUpDTO dto);
    int addTutor(SignUpDTO dto);
}
