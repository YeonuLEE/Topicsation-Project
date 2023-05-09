package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.SignUpDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ISignUpDAO {
    // 회원가입시 입력한 email이 db에 저장되어있는지 확인
    public int checkEmailDAO(String email);

    // 회원가입시 이메일 인증을 받지않은 회원정보가 있는지 확인
    public int checkEmailAuthDAO(String email);

    // 이메일 인증 못한 id 재가입시 삭제
    public int deleteNotAuthTuteeDAO(String email);

    public int deleteNotAuthTutorDAO(String email);

    // 신규회원이면 db에 등록해서 추가된 행의 수가 1인지 확인
    public int addTuteeDAO(SignUpDTO signUpDTO);

    public int addTutorDAO1(SignUpDTO signUpDTO);
    public int addTutorDAO2(SignUpDTO signUpDTO);

    // 업로드된 파일명을 user_id로 사용
    public String getUserId(String email);
    // 인증 코드 저장 메서드
    int saveEmailCode(@Param("email") String email, @Param("emailCode") String emailCode);
    //  인증코드 가져오기
    int getEmailCode(@Param("email") String email);
    // 회원 삭제 하기

}
