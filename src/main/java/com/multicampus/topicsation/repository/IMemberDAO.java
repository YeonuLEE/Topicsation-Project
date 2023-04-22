package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.TutorMyPageDTO;
import com.multicampus.topicsation.dto.TutorMypageScheduleDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMemberDAO {

    TutorMyPageDTO viewTutor(String user_id);
    int modifyTutor(TutorMyPageDTO tutorMyPageDTO);
    TutorMypageScheduleDTO tutorProfile(String user_id);
    List<TutorScheduleDTO> scheduleDTO(String tutor_id);
}
