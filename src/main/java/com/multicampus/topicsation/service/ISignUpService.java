package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SignUpDTO;

import java.util.Map;

public interface ISignUpService {
    boolean signUpProcess(SignUpDTO signUpDTO);
}
