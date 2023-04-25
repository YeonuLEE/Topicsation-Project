package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageDTO{
    private String user_id;
    private String email;
    private String name;
    private String password;
    private String interest1;
    private String interest2;
    private String role;
    private String gender;
    private String nationality;
    private String regi_date;
    private String info;
    private int like;
    private String profileimg;
}
