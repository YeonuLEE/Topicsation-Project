package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ILoginDAO {
    LoginDTO login(String email); //로그인
    int changePassword(LoginDTO loginDTO); //비밀번호 변경
    int checkApproval(String userId); //튜터 승인여부 확인
    int deleteUnauthenticatedUsers(); //이메일 인증 받지 않은 회원 정보 삭제
}
