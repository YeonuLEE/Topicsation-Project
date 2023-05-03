package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    private String user_id;
    private String email;
    private String name;
    private String password;
    private String gender;
    private String nationality;
    private String firstInterest;
    private String secondInterest;
    private String role;
    private String certificate;
    private int email_auth;
    private MultipartFile file;
}
