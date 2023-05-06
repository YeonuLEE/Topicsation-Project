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
    public String check_password(String user_id) {
        return dao.checkPass(user_id);
    }

    @Override
    public String view(String user_id) {

        MyPageDTO myPageDTO;
        JSONObject jsonObject = new JSONObject();
        String role = dao.checkRole(user_id);
        if (role.equals("tutor")) {
            myPageDTO = dao.viewTutor(user_id);

            jsonObject.put("profileImg", myPageDTO.getProfileimg());
            jsonObject.put("name", myPageDTO.getName());
            jsonObject.put("email", myPageDTO.getEmail());
            jsonObject.put("nationality", myPageDTO.getNationality());
            jsonObject.put("interest1", myPageDTO.getInterest1());
            jsonObject.put("interest2", myPageDTO.getInterest2());
            jsonObject.put("genderRadios", myPageDTO.getGender());
            jsonObject.put("memo",myPageDTO.getInfo());
            jsonObject.put("password",myPageDTO.getPassword());

            return jsonObject.toJSONString();
        } else if (role.equals("tutee")) {
            myPageDTO = dao.viewTutee(user_id);
            jsonObject.put("tutor-name", myPageDTO.getName());
            jsonObject.put("name", myPageDTO.getName());
            jsonObject.put("email", myPageDTO.getEmail());
            jsonObject.put("interest1", myPageDTO.getInterest1());
            jsonObject.put("interest2", myPageDTO.getInterest2());

            return jsonObject.toJSONString();
        }else{
            List<MyPageDTO> list = dao.viewAdmin();
            JSONArray jsonArray = new JSONArray();

            for (MyPageDTO dto : list) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("userId", dto.getUser_id());
                jsonObject2.put("tutorName", dto.getName());
                jsonObject2.put("approlDate", dto.getRegi_date());
                jsonObject2.put("file", dto.getCertificate());

                jsonArray.add(jsonObject2);
            }
            return jsonArray.toJSONString();
        }
    }

    @Override
    public void modify(JSONObject jsonObject,String userId) {
        MyPageDTO myPageDTO = new MyPageDTO();
        String role = dao.checkRole(userId);

        myPageDTO.setUser_id(userId);
        myPageDTO.setName(jsonObject.get("$name").toString());
        myPageDTO.setInterest1(jsonObject.get("$interest1").toString());
        myPageDTO.setInterest2(jsonObject.get("$interest2").toString());

        if (role.equals("tutee")) {
            dao.modifyTutee(myPageDTO);
        }else if(role.equals("tutor")){
            myPageDTO.setGender(jsonObject.get("$gander").toString());
            myPageDTO.setNationality(jsonObject.get("$nationality").toString());
            myPageDTO.setInfo(jsonObject.get("$memo").toString());
            dao.modifyTutor(myPageDTO);
            dao.modifyTutor2(myPageDTO);
        }
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
    public void delete(String user_id) {
        String role = dao.checkRole(user_id);
        if (role.equals("tutee")) {
            dao.deleteTutee(user_id);
            dao.chageClass(user_id);
            dao.tuteeDislike(user_id);
        } else if (role.equals("tutor")) {
            dao.deleteClass(user_id);
            dao.tutorDislike(user_id);
            dao.deleteTutorInfo(user_id);
            dao.deleteTutor(user_id);
        }
    }

    @Override
    public void chang_profileImg(String user_id, String fileName) {
        dao.changProfileImg(user_id, fileName);
    }

    @Override
    public String schedule_tutee(String user_id) {
        MypageScheduleDTO mypageScheduleDTO =  dao.tuteeProfile(user_id);
        List<ClassDTO> classDTOList = dao.scheduleTutee(user_id);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tutee_name", mypageScheduleDTO.getName());

        for (ClassDTO dto : classDTOList) {
            JSONObject object = new JSONObject();
            object.put("class_id", dto.getClass_id());
            object.put("class_date", dto.getClass_date());
            object.put("class_time", dto.getClass_time());
            object.put("tutor_name", dto.getName());
            object.put("class_id", dto.getClass_id());

            jsonArray.add(object);
        }
        jsonObject.put("schedules", jsonArray);
        String jsonString = jsonObject.toString();

        return jsonString;
    }

    @Override
    public String history_tutee(String user_id) {

        MypageScheduleDTO mypageScheduleDTO = dao.tuteeProfile(user_id);
        List<ClassDTO> dtoList = dao.historyTutee(user_id);

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObject.put("name", mypageScheduleDTO.getName());
        jsonObject.put("user_id", user_id);

        for (ClassDTO dto : dtoList) {
            JSONObject object = new JSONObject();

            object.put("class_date",dto.getClass_date());
            object.put("tutor_name",dto.getName());
            String url = dto.getUrl();
            if(url != null){
                String[] urlList = url.split(",");
                object.put("memo1",urlList[0]);
                object.put("memo2",urlList[1]);
                object.put("memo3",urlList[2]);
            }

            jsonArray.add(object);
        }

        jsonObject.put("history", jsonArray);
        String jsonString = jsonObject.toString();

        return jsonString;
    }

    @Override
    public void schedule_cancel(String class_id) {
        dao.cancelSchedule(class_id);
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

