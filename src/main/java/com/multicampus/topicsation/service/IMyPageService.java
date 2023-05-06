package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public interface IMyPageService {
    String check_password(String userId);

    void success(String userId);
    void fail(String userId);

    String view(String userId);
    void modify(JSONObject jsonObject,String userId);
    MypageScheduleDTO schedule_tutor(Map<String, Object> paramMap, MypageScheduleDTO mypageScheduleDTO);
    int scheduleUpdate(JSONObject jsonUserInfo, JSONArray jsonSchedule);
    void delete(String userId);
    void chang_profileImg(String user_id, String fileName);

    String schedule_tutee(String userId);
    String history_tutee(String userId);
    void schedule_cancel(String userId);
}
