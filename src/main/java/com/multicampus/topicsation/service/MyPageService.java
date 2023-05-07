package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MyPageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
import com.multicampus.topicsation.repository.IMemberDAO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyPageService implements IMyPageService{

    @Autowired
    private IMemberDAO dao;

    @Autowired
    private IS3FileService s3FileService;

    @Override
    public String check_password(String userId) {
        return dao.checkPass(userId);
    }

    @Override
    public String view(String userId) {
        MyPageDTO myPageDTO;
        JSONObject jsonObject = new JSONObject();
        if (dao.checkRole(userId).equals("tutor")) {
            myPageDTO = dao.viewTutor(userId);
            jsonObject.put("profileImg", s3FileService.getImageUrl("asset", "profile", myPageDTO.getProfileimg()));
            jsonObject.put("name", myPageDTO.getName());
            jsonObject.put("email", myPageDTO.getEmail());
            jsonObject.put("nationality", myPageDTO.getNationality());
            jsonObject.put("interest1", myPageDTO.getInterest1());
            jsonObject.put("interest2", myPageDTO.getInterest2());
            jsonObject.put("genderRadios", myPageDTO.getGender());
            jsonObject.put("memo",myPageDTO.getInfo());
            jsonObject.put("password",myPageDTO.getPassword());
            return jsonObject.toJSONString();
        }
        else if (dao.checkRole(userId).equals("tutee")) {
            myPageDTO = dao.viewTutee(userId);
            jsonObject.put("tutor-name", myPageDTO.getName());
            jsonObject.put("name", myPageDTO.getName());
            jsonObject.put("email", myPageDTO.getEmail());
            jsonObject.put("interest1", myPageDTO.getInterest1());
            jsonObject.put("interest2", myPageDTO.getInterest2());

            return jsonObject.toJSONString();
        }
        else{
            List<MyPageDTO> list = dao.viewAdmin();
            JSONArray jsonArray = new JSONArray();

            for (MyPageDTO dto : list) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("userId", dto.getUserId());
                jsonObject2.put("tutorName", dto.getName());
                jsonObject2.put("approlDate", dto.getRegi_date());
                jsonObject2.put("file", s3FileService.generatePresignedUrl(
                        "asset", "certificate" + "/" + dto.getCertificate()).toString());
                jsonArray.add(jsonObject2);
            }
            return jsonArray.toJSONString();
        }
    }

    @Override
    public void modify(JSONObject jsonObject,String userId) {
        MyPageDTO myPageDTO = new MyPageDTO();
        String role = dao.checkRole(userId);

        myPageDTO.setUserId(userId);
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
    public MyPageScheduleDTO schedule_tutor(Map<String, Object> paramMap) {
        MyPageScheduleDTO mypageScheduleDTO = dao.tutorProfile(paramMap.get("tutorId").toString());
        mypageScheduleDTO.setScheduleDTOList(dao.tutorSchedule(paramMap));
        return mypageScheduleDTO;
    }

    @Override
    public int scheduleUpdate(JSONObject jsonUserInfo, JSONArray jsonSchedule) {
        Map<String, Object> paramMap2 = new HashMap<>();
        paramMap2.put("tutor_id", jsonUserInfo.get("user_id"));
        paramMap2.put("class_date", jsonUserInfo.get("class_date"));

        String hashedPw = dao.checkPass(jsonUserInfo.get("user_id").toString());

        if ( BCrypt.checkpw(jsonUserInfo.get("password").toString(), hashedPw)){
            dao.scheduleDelete(paramMap2);
            if (!jsonSchedule.isEmpty()) {
                int countResult = 0;
                Map<String, String> scheduleMap = new HashMap<>();
                for (Object o : jsonSchedule) {
                    JSONObject schedule = (JSONObject) o;
                    scheduleMap.put("tutor_id", paramMap2.get("tutor_id").toString());
                    scheduleMap.put("class_date", paramMap2.get("class_date").toString());
                    scheduleMap.put("class_time", schedule.get("class_time").toString());
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
    public void delete(String userId) {
        String role = dao.checkRole(userId);
        if (role.equals("tutee")) {
            dao.deleteTutee(userId);
        } else if (role.equals("tutor")) {
            dao.deleteTutor(userId);
        }
    }

    @Override
    public boolean change_profileImg(String userId, MultipartFile file) {
        String bucketName = "asset";
        String folderName = "profile";
        String fileExtension = getFileExtension(file.getOriginalFilename()); //확장자 가져오기
        String fileName = userId + "." + fileExtension;
        String objectKey = folderName + "/" + fileName;

        // 확장자를 제외한 파일 이름
        String fileNameWithoutExtension = userId;

        // 기존 파일이 있는지 확인
        boolean fileExists = s3FileService.isFileExists(bucketName, folderName, fileNameWithoutExtension);
        System.out.println(fileExists);
        if (fileExists) {
            // 기존 파일이 있으면 삭제
            String existingObjectKey = folderName + "/" + fileNameWithoutExtension;
            // NCP에 기존 파일 삭제
            s3FileService.deleteFile(bucketName, existingObjectKey);
            System.out.println("파일 삭제 완료");
        }

        s3FileService.uploadFile(bucketName, objectKey, file);
        dao.changeProfileImg(userId, fileName);
        return true;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    @Override
    public String schedule_tutee(String userId) {
        MyPageScheduleDTO mypageScheduleDTO =  dao.tuteeProfile(userId);
        List<ClassDTO> classDTOList = dao.scheduleTutee(userId);

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
    public String historyTutee(String userId) {

        MyPageScheduleDTO mypageScheduleDTO = dao.tuteeProfile(userId);
        List<ClassDTO> dtoList = dao.historyTutee(userId);

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObject.put("name", mypageScheduleDTO.getName());
        jsonObject.put("user_id", userId);

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

        return jsonObject.toString();
    }

    @Override
    public void schedule_cancel(String class_id) {
        dao.cancelSchedule(class_id);
    }

    @Override
    public void success(String userId) {
        dao.successAdmin(userId);
    }

    @Override
    public void fail(String userId) {
        dao.failAdmin2(userId);
        dao.failAdmin(userId);
    }
}

