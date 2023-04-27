package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    private String user_id;
    private String email;
    private String name;
    private String password;
    private String tutor_image;
    private String like;
    private String gender;
    private String nationality;
    private String firstInterest;
    private String secondInterest;
    private String regi_date;
    private String role;
    private String certificate;
    private boolean email_auth;
}
