package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.TutorMypageScheduleDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;

import java.util.List;

public interface IMyPageService {
    String check_role(String user_id);

    MyPageDTO view_tutor(String user_id);
    int modify_tutor(MyPageDTO MyPageDTO);
    TutorMypageScheduleDTO tutorProfile(String user_id);
    List<TutorScheduleDTO> schedule(String user_id);
}
