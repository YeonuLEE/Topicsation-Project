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
    private String nationality;
    private String interest1;
    private String interest2;
    private String regi_date;
    private MemberRole role;
    private boolean email_auth;
}
