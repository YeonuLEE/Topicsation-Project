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
    int modifyTutor2(MyPageDTO myPageDTO);
    MypageScheduleDTO tutorProfile(String user_id);
    List<ClassDTO> scheduleDTO(String tutor_id);
    void deleteTutor(String user_id);
    void deleteTutorInfo(String user_id);
    void deleteClass(String user_id);
    void deleteDislike(String user_id);

    MyPageDTO viewTutee(String user_id);
    int modifyTutee(MyPageDTO myPageDTO);
    MypageScheduleDTO tuteeProfile(String user_id);
    List<ClassDTO> scheduleTuteeDTO(String user_id);
    List<ClassDTO> historyTuteeDTO(String user_id);
    void deleteTutee(String user_id);
    void chageClass(String user_id);
    void cancelSchedule(String user_id);

    List<MyPageDTO> viewAdmin();

    TutorViewDTO tutorInfo(Map <String,Object> paramMap);
    List<ClassDTO> tutorSchedule(Map <String,Object> paramMap);
    int classReservate(Map <String,Object> paramMap);

}
