package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;

import java.util.List;

public interface IMyPageService {
    String check_role(String user_id);

    MyPageDTO view_tutor(String user_id);
    int modify_tutor(MyPageDTO MyPageDTO);
    MypageScheduleDTO tutorProfile(String user_id);
    List<ClassDTO> schedule_tutor(String user_id);

    MyPageDTO view_tutee(String user_id);
    int modify_tutee(MyPageDTO myPageDTO);
    MypageScheduleDTO tuteeProfile(String user_id);
    List<ClassDTO> schedule_tutee(String user_id);
    List<ClassDTO> history_tutee(String user_id);

}
