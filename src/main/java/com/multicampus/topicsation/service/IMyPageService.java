package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.TutorMyPageDTO;
import com.multicampus.topicsation.dto.TutorMypageScheduleDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;

import java.util.List;

public interface IMyPageService {
    TutorMyPageDTO view(String user_id);
    int modify(TutorMyPageDTO tutorMyPageDTO);
    TutorMypageScheduleDTO tutorProfile(String user_id);
    List<TutorScheduleDTO> schedule(String user_id);
}
