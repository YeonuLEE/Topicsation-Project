package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
import com.multicampus.topicsation.repository.IMemberDAO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyPageService implements IMyPageService{

    @Autowired
    IMemberDAO dao;

    @Override
    public String check_role(String user_id) {
        return dao.checkRole(user_id);
    }

    @Override
    public String check_password(String user_id) {
        return dao.checkPass(user_id);
    }

    @Override
    public MyPageDTO view_tutor(String user_id) {
        return dao.viewTutor(user_id);
    }

    @Override
    public int modify_tutor(MyPageDTO myPageDTO) {
        dao.modifyTutor(myPageDTO);
        dao.modifyTutor2(myPageDTO);
        return 0;
    }

    @Override
    public MypageScheduleDTO schedule_tutor(Map<String, Object> paramMap, MypageScheduleDTO mypageScheduleDTO) {
        mypageScheduleDTO = dao.tutorProfile(paramMap.get("tutorId").toString());
        mypageScheduleDTO.setScheduleDTOList(dao.tutorSchedule(paramMap));
        return mypageScheduleDTO;
    }

    @Override
    public int scheduleUpdate(JSONObject jsonUserInfo, JSONArray jsonSchedule) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", jsonUserInfo.get("user_id"));
        paramMap.put("password", jsonUserInfo.get("password"));

        Map<String, Object> paramMap2 = new HashMap<>();
        paramMap2.put("tutor_id", jsonUserInfo.get("user_id"));
        paramMap2.put("class_date", jsonUserInfo.get("class_date"));

        int findResult;
        String hashedPw = dao.checkPass(jsonUserInfo.get("user_id").toString());
        if ( BCrypt.checkpw(jsonUserInfo.get("password").toString(), hashedPw)){
            findResult = 1;
        }else{
            findResult = 0;
        }

        System.out.println("findResult : " + findResult);
        if (findResult == 1) {
            dao.scheduleDelete(paramMap2);
            if (!jsonSchedule.isEmpty()) {
                int countResult = 0;
                Map<String, Object> scheduleMap = new HashMap<>();
                for (int i = 0; i < jsonSchedule.size(); i++) {
                    JSONObject schedule = new JSONObject();
                    schedule = (JSONObject) jsonSchedule.get(i);
                    scheduleMap.put("tutor_id", paramMap2.get("tutor_id"));
                    scheduleMap.put("class_date", paramMap2.get("class_date"));
                    scheduleMap.put("class_time", schedule.get("class_time"));
                    countResult += dao.scheduleUpdate(scheduleMap);
                }
                return 1; //스케쥴 업데이트 완료
            } else {
                return 0; // 업데이트할 스케쥴이 없음
            }
        }
        return 2; // 비밀번호 오류
    }

    @Override
    public void delete_tutor(String user_id) {
        dao.deleteClass(user_id);
        dao.tutorDislike(user_id);
        dao.deleteTutorInfo(user_id);
        dao.deleteTutor(user_id);

    }

    @Override
    public void chang_profileImg(String user_id, String fileName) {
        System.out.println(fileName);
        dao.changProfileImg(user_id, fileName);
    }

    @Override
    public MyPageDTO view_tutee(String user_id) { return dao.viewTutee(user_id); }

    @Override
    public int modify_tutee(MyPageDTO myPageDTO) {
        return dao.modifyTutee(myPageDTO);
    }

    @Override
    public MypageScheduleDTO tuteeProfile(String user_id) {
        return dao.tuteeProfile(user_id);
    }

    @Override
    public List<ClassDTO> schedule_tutee(String user_id) {
        return dao.scheduleTutee(user_id);
    }

    @Override
    public List<ClassDTO> history_tutee(String user_id) {
        return dao.historyTutee(user_id);
    }

    @Override
    public void delete_tutee(String user_id) {
        dao.deleteTutee(user_id);
        dao.chageClass(user_id);
        dao.tuteeDislike(user_id);
    }

    @Override
    public void schedule_cancel(String class_id) {
        dao.cancelSchedule(class_id);
    }

    @Override
    public List<MyPageDTO> view_admin() {
        List<MyPageDTO> list =dao.viewAdmin();
        return list;
    }

    @Override
    public void success(String user_id) {
        dao.successAdmin(user_id);
    }

    @Override
    public void fail(String user_id) {
        dao.failAdmin2(user_id);
        dao.failAdmin(user_id);
    }
}

