package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.SignUpDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISignUpDAO {
    // 회원가입시 입력한 email이 db에 저장되어있는지 확인(or 같은 email이 count가 0보다 큰 지 확인)
    public int checkEmailDAO(String email);

    // 신규회원이면 db에 등록해서 추가된 행의 수가 1인지 확인
    public int addTuteeDAO(SignUpDTO dto);

    //    public int addUserDAO(Map<String, String> map);
    public int addTutorDAO(SignUpDTO dto); //sql 쿼리문를 실행한 결과로 영향받은 행의 수를 반환
}
