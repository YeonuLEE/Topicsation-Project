package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ILoginDAO {
    LoginDTO login(Map<String, String> map); //로그인
    int changePassword(LoginDTO loginDTO); //비밀번호 변경
}
