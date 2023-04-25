package com.multicampus.topicsation.dto;

// 멤버의 정보를 갖는 DTO, 튜티 테이블과의 조인은 생각해 볼 것

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String user_id;
    private String email;
    private String name;
    private String password;
    private String interest1;
    private String interest2;
    private String regi_date;
    private String role;
    private boolean email_auth;
}
