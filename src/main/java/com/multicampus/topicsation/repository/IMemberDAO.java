package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IMemberDAO {
    String checkRole(String user_id);

    MyPageDTO viewTutor(String user_id);
    int modifyTutor(MyPageDTO myPageDTO);
    MypageScheduleDTO tutorProfile(String user_id);
    List<ClassDTO> scheduleDTO(String tutor_id);

    MyPageDTO viewTutee(String user_id);
    int modifyTutee(MyPageDTO myPageDTO);
    
    TutorViewDTO tutorInfo(Map <String,Object> paramMap);
    List<ClassDTO> tutorSchedule(Map <String,Object> paramMap);
    int classReservate(Map <String,Object> paramMap);

}
