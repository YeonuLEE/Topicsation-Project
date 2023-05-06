package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IMyPageService {
    String check_password(String user_id);

    void success(String user_id);
    void fail(String user_id);

    String view(String user_id);
    void modify(JSONObject jsonObject,String userId);
    MypageScheduleDTO schedule_tutor(Map<String, Object> paramMap, MypageScheduleDTO mypageScheduleDTO);
    int scheduleUpdate(JSONObject jsonUserInfo, JSONArray jsonSchedule);
    void delete(String user_id);
    void change_profileImg(String user_id, MultipartFile file);

    String schedule_tutee(String user_id);
    String history_tutee(String user_id);
    void schedule_cancel(String class_id);
}
