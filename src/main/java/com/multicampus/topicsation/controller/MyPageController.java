package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.ClassDTO;
import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.service.IMyPageService;
import com.multicampus.topicsation.token.JwtUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private IMyPageService service;

    @Autowired
    private JwtUtils jwtUtils;


    @GetMapping("/tutee")
    public String tuteePage() {
        return "html/dashboard/myPage-tutees_Information";
    }

    @GetMapping("/tutor")
    public String tutorPage() {
        return "html/dashboard/myPage-tutors_Information";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "html/dashboard/myPage-admin";
    }

    @GetMapping("/tutee/schedule")
    public String tuteeSchedulePage() {
        return "html/dashboard/myPage-tutees_Schedule";
    }

    @GetMapping("/tutor/schedule")
    public String tutorSchedulePage() {
        return "html/dashboard/myPage-tutors_Schedule";
    }

    @GetMapping("/tutee/history")
    public String historyPage() {
        return "html/dashboard/myPage-tutees_CourseHistory";
    }


    @RestController
    @RequestMapping("/mypage")
    public class MyPageRestController {

        @GetMapping("/{user_id}")
        public String myPage(HttpServletRequest request) {
            return jwtUtils.authByRole(request, "/mypage/tutee", "/mypage/tutor", "/mypage/admin");
        }

        @GetMapping("/{user_id}/schedule")
        public String schedulePage(HttpServletRequest request) {
            return jwtUtils.authByRole(request, "/mypage/tutee/schedule", "/mypage/tutor/schedule");
        }

        @GetMapping("/{user_id}/history")
        public String historyPage(HttpServletRequest request) {
            return jwtUtils.authByRole(request, "/mypage/tutee/history");
        }

        @GetMapping("/admin/get")
        public String adminPage() {
            List<MyPageDTO> list = service.view_admin();
            JSONArray jsonArray = new JSONArray();

            for (MyPageDTO dto : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", dto.getUser_id());
                jsonObject.put("tutorName", dto.getName());
                jsonObject.put("approlDate", dto.getRegi_date());
                jsonObject.put("file", dto.getCertificate());

                jsonArray.add(jsonObject);
            }

            String jsonString = jsonArray.toString();

            return jsonString;
        }

        @PostMapping("/admin/success")
        public String adminSuccess(@RequestBody String userId) {
            service.success(userId);
            return null;
        }

        @PostMapping("/admin/fail")
        public String adminFail(@RequestBody String userId) {
            service.fail(userId);
            return null;
        }

        @GetMapping("/{user_id}/get")
        public String myPage(@PathVariable("user_id") String userId) {
            MyPageDTO myPageDTO;
            JSONObject jsonObject = new JSONObject();
            String role = service.check_role(userId);
            if (role.equals("tutor")) {
                myPageDTO = service.view_tutor(userId);

                jsonObject.put("profileImg", myPageDTO.getProfileimg());
                jsonObject.put("name", myPageDTO.getName());
                jsonObject.put("email", myPageDTO.getEmail());
                jsonObject.put("nationality", myPageDTO.getNationality());
                jsonObject.put("interest1", myPageDTO.getInterest1());
                jsonObject.put("interest2", myPageDTO.getInterest2());
                jsonObject.put("genderRadios", myPageDTO.getGender());
//                jsonObject.put("password",myPageDTO.getPassword());

            } else if (role.equals("tutee")) {
                myPageDTO = service.view_tutee(userId);
                jsonObject.put("tutor-name", myPageDTO.getName());
                jsonObject.put("name", myPageDTO.getName());
                jsonObject.put("email", myPageDTO.getEmail());
                jsonObject.put("interest1", myPageDTO.getInterest1());
                jsonObject.put("interest2", myPageDTO.getInterest2());

            }

            return jsonObject.toJSONString();
        }

        @PostMapping("/{user_id}/passCheck")
        public boolean passCheck(@RequestBody Map<String, String> params, @PathVariable("user_id") String userId) {
            String password = params.get("password");
            System.out.println(password);
            String hashPass = service.check_password(userId);
            System.out.println(hashPass);
            return BCrypt.checkpw(password, hashPass);
        }

        @PostMapping("/{user_id}/post")
        public String myPageModify(@RequestBody JSONObject jsonObject, @PathVariable("user_id") String userId) {
            MyPageDTO myPageDTO = new MyPageDTO();
            String role = service.check_role(userId);

            myPageDTO.setUser_id(userId);
            myPageDTO.setName(jsonObject.get("$name").toString());
            myPageDTO.setInterest1(jsonObject.get("$interest1").toString());
            myPageDTO.setInterest2(jsonObject.get("$interest2").toString());

            if (role.equals("tutee")) {
                service.modify_tutee(myPageDTO);
            } else if (role.equals("tutor")) {
                myPageDTO.setProfileimg(jsonObject.get("$profileImg").toString());
                myPageDTO.setNationality(jsonObject.get("$nationality").toString());
                service.modify_tutor(myPageDTO);
            }
            return null;
        }

        @PostMapping("/{user_id}/delete")
        public String myPageDelete(@PathVariable("user_id") String userId) {
            String role = service.check_role(userId);
            if (role.equals("tutee")) {
                service.delete_tutee(userId);
            } else if (role.equals("tutor")) {
                service.delete_tutor(userId);
            }

            return null;
        }

        @GetMapping("/{user_id}/schedule/get")
        public String schedulePage(@PathVariable("user_id") String userId) {
            MypageScheduleDTO mypageScheduleDTO = service.tuteeProfile(userId);
            List<ClassDTO> classDTOList = service.schedule_tutee(userId);

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

        @PutMapping("/{user_id}/schedule/cancel")
        public String scheduleCancel(@RequestBody JSONObject jsonObject) {
            service.schedule_cancel(jsonObject.get("$class_id").toString());
//            String class_id = jsonObject.get("$class_id").toString();
//            System.out.println(class_id);
            return null;
        }

        @GetMapping("/{user_id}/schedule/getCalendar")
        public String schedulePageCalendar(@PathVariable("user_id") String tutorId,
                                           @RequestParam("calendarDate") String calendarDate) {

            MypageScheduleDTO mypageScheduleDTO = new MypageScheduleDTO();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("tutorId", tutorId);
            paramMap.put("classDate", calendarDate);

            mypageScheduleDTO = service.schedule_tutor(paramMap, mypageScheduleDTO);

            System.out.println(mypageScheduleDTO.getUser_id());
            System.out.println(mypageScheduleDTO.getName());
            System.out.println(mypageScheduleDTO.getProfileimg());

            JSONObject jsonObject_info = new JSONObject();
            JSONArray jsonArray_schedule = new JSONArray();

            jsonObject_info.put("user_id", tutorId);
            jsonObject_info.put("name", mypageScheduleDTO.getName());
            jsonObject_info.put("picture", mypageScheduleDTO.getProfileimg());

            for (int i = 0; i < mypageScheduleDTO.getScheduleDTOList().size(); i++) {
                JSONObject jsonObject_schedule = new JSONObject();
                jsonObject_schedule.put("class_id", mypageScheduleDTO.getScheduleDTOList().get(i).getClass_id());
                jsonObject_schedule.put("class_date", mypageScheduleDTO.getScheduleDTOList().get(i).getClass_date());
                jsonObject_schedule.put("class_time", mypageScheduleDTO.getScheduleDTOList().get(i).getClass_time());
                jsonObject_schedule.put("tutee_id", mypageScheduleDTO.getScheduleDTOList().get(i).getTutee_id());
                jsonObject_schedule.put("tutor_id", mypageScheduleDTO.getScheduleDTOList().get(i).getTutor_id());
                jsonObject_schedule.put("name", mypageScheduleDTO.getScheduleDTOList().get(i).getName());
                jsonArray_schedule.add(jsonObject_schedule);
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("tutor_info", jsonObject_info);
            jsonObject.put("schedule", jsonArray_schedule);

            String jsonString = jsonObject.toJSONString();
            System.out.println(jsonString);

            return jsonString;
        }

        @PostMapping("/{user_id}/schedule/postCalender")
        public String schedulePost(@RequestBody String jsonString) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            JSONObject jsonUserInfo;
            JSONArray jsonSchedule;
            int result = 0;
            try {
                jsonObject = (JSONObject) parser.parse(jsonString);
                jsonUserInfo = (JSONObject) jsonObject.get("user_info");
                jsonSchedule = (JSONArray) jsonObject.get("schedule");
                System.out.println("jsonUserInfo : " + jsonUserInfo);
                System.out.println("jsonSchedule : " + jsonSchedule);
                result = service.scheduleUpdate(jsonUserInfo, jsonSchedule);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result == 1 || result == 0)
                return "success";
            else
                return "fail";
        }

        @GetMapping("/{user_id}/history/get")
        public String historyPage(@PathVariable("user_id") String user_id) {
            MypageScheduleDTO mypageScheduleDTO = service.tuteeProfile(user_id);
            List<ClassDTO> dtoList = service.history_tutee(user_id);

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            jsonObject.put("name", mypageScheduleDTO.getName());
            jsonObject.put("user_id", user_id);

            for (ClassDTO dto : dtoList) {
                JSONObject object = new JSONObject();
                object.put("class_date", dto.getClass_date());
                object.put("tutor_name", dto.getName());
                object.put("memo", null);

                jsonArray.add(object);
            }

            jsonObject.put("history", jsonArray);
            String jsonString = jsonObject.toString();

            return jsonString;
        }
    }
}

