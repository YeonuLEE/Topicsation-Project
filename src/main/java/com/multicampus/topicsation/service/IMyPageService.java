package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public interface IMyPageService {
    String check_role(String user_id);
    String check_password(String user_id);

    List<MyPageDTO> view_admin();
    void success(String user_id);
    void fail(String user_id);

    MyPageDTO view_tutor(String user_id);
    int modify_tutor(MyPageDTO myPageDTO);
    MypageScheduleDTO schedule_tutor(Map<String, Object> paramMap, MypageScheduleDTO mypageScheduleDTO);
    int scheduleUpdate(JSONObject jsonUserInfo, JSONArray jsonSchedule);
    void delete_tutor(String user_id);
    void chang_profileImg(String user_id);

    MyPageDTO view_tutee(String user_id);
    int modify_tutee(MyPageDTO myPageDTO);
    MypageScheduleDTO tuteeProfile(String user_id);
    List<ClassDTO> schedule_tutee(String user_id);
    List<ClassDTO> history_tutee(String user_id);
    void delete_tutee(String user_id);
    void schedule_cancel(String class_id);
}
