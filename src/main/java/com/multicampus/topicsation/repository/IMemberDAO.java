package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.*;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IMemberDAO {
    String checkRole(String user_id);

    MyPageDTO viewTutor(String user_id);
    int modifyTutor(MyPageDTO myPageDTO);
    TutorMypageScheduleDTO tutorProfile(String user_id);
    List<TutorScheduleDTO> scheduleDTO(String tutor_id);

    MyPageDTO viewTutee(String user_id);
    int modifyTutee(MyPageDTO myPageDTO);
    
    TutorViewDTO tutorInfo(Map <String,Object> paramMap);
    List<TutorScheduleDTO> tutorSchedule(Map <String,Object> paramMap);
    List<TutorReviewDTO> tutorReview(String tutorId);
    int classReservate(Map <String,Object> paramMap);

}
