package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IMemberDAO {
    String checkRole(String userId);
    String checkPass(String userId);

    List<MyPageDTO> viewAdmin();
    void successAdmin(String userId);
    void failAdmin(String userId);
    void failAdmin2(String userId);

    MyPageDTO viewTutor(String userId);
    int modifyTutor(MyPageDTO myPageDTO);
    int modifyTutor2(MyPageDTO myPageDTO);
    
    MypageScheduleDTO tutorProfile(String userId);
    String checkProfileImg(String userId);
    void changeProfileImg(@Param("user_id") String userId, @Param("fileName") String fileName);


    List<ClassDTO> tutorSchedule(Map<String, Object> paramMap);
    int findUser(Map<String, Object> paramMap);
    void scheduleDelete(Map<String, Object> scheduleMap);
    int scheduleUpdate(Map<String, Object> scheduleMap);

    List<ClassDTO> schedule(String userId);
    void deleteClass(String userId);
    void tutorDislike(String userId);
    void deleteTutorInfo(String userId);
    void deleteTutor(String userId);

    MyPageDTO viewTutee(String userId);
    int modifyTutee(MyPageDTO myPageDTO);
    MypageScheduleDTO tuteeProfile(String userId);
    List<ClassDTO> scheduleTutee(String userId);
    List<ClassDTO> historyTutee(String userId);
    void deleteTutee(String userId);
    void chageClass(String userId);
    void cancelSchedule(String class_id);
    void tuteeDislike(String userId);

}
