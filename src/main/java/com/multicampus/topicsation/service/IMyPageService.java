package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageScheduleDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IMyPageService {
    String check_password(String userId);

    void success(String userId);
    void fail(String userId);

    String view(String userId);
    void modify(JSONObject jsonObject,String userId);
    MyPageScheduleDTO schedule_tutor(Map<String, Object> paramMap);
    int scheduleUpdate(JSONObject jsonUserInfo, JSONArray jsonSchedule);

    void delete(String user_id);
    boolean change_profileImg(String user_id, MultipartFile file);

    String schedule_tutee(String userId);
    String historyTutee(String userId);
    void schedule_cancel(String userId);
}
