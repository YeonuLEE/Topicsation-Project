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
    int successAdmin(String userId);
    int failAdmin(String userId);

    MyPageDTO viewTutor(String userId);
    int modifyTutor(MyPageDTO myPageDTO);
    int modifyTutor2(MyPageDTO myPageDTO);
    
    MyPageScheduleDTO tutorProfile(String userId);
    void changeProfileImg(@Param("userId") String userId, @Param("fileName") String fileName);


    List<ClassDTO> tutorSchedule(Map<String, Object> paramMap);
    void scheduleDelete(Map<String, Object> scheduleMap);
    int scheduleUpdate(Map<String, String> scheduleMap);

    List<ClassDTO> schedule(String userId);
    void deleteClass(String userId);
    int deleteTutor(String userId);

    MyPageDTO viewTutee(String userId);
    int modifyTutee(MyPageDTO myPageDTO);
    MyPageScheduleDTO tuteeProfile(String userId);
    List<ClassDTO> scheduleTutee(String userId);
    List<ClassDTO> historyTutee(String userId);
    int deleteTutee(String userId);
    void cancelSchedule(String class_id);

}
