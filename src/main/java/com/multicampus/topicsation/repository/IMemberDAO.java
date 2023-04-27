package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IMemberDAO {
    String checkRole(String user_id);

    MyPageDTO viewTutor(String user_id);
    int modifyTutor(MyPageDTO myPageDTO);
    int modifyTutor2(MyPageDTO myPageDTO);
    MypageScheduleDTO tutorProfile(String user_id);
    List<ClassDTO> scheduleDTO(String tutor_id);

    MyPageDTO viewTutee(String user_id);
    int modifyTutee(MyPageDTO myPageDTO);
}
