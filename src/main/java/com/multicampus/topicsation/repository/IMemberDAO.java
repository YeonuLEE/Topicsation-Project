package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IMemberDAO {
    String checkRole(String user_id);

    List<MyPageDTO> viewAdmin();
    void successAdmin(String user_id);
    void failAdmin(String user_id);
    void failAdmin2(String user_id);

    MyPageDTO viewTutor(String user_id);
    int modifyTutor(MyPageDTO myPageDTO);
    int modifyTutor2(MyPageDTO myPageDTO);
    MypageScheduleDTO tutorProfile(String user_id);
    List<ClassDTO> schedule(String tutor_id);
    void deleteClass(String user_id);
    void tutorDislike(String user_id);
    void deleteTutorInfo(String user_id);
    void deleteTutor(String user_id);

    MyPageDTO viewTutee(String user_id);
    int modifyTutee(MyPageDTO myPageDTO);
    MypageScheduleDTO tuteeProfile(String user_id);
    List<ClassDTO> scheduleTutee(String user_id);
    List<ClassDTO> historyTutee(String user_id);
    void deleteTutee(String user_id);
    void chageClass(String user_id);
    void cancelSchedule(String class_id);
    void tuteeDislike(String user_id);

}
