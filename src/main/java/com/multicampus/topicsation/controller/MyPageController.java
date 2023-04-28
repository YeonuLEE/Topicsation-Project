package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.ClassDTO;
import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.service.IMyPageService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private IMyPageService service;

    @GetMapping("/admin")
    public String adminPage() {
        return "html/dashboard/myPage-admin";
    }

    @GetMapping("/{user_id}")
    public String myPage(@PathVariable("user_id") String userId) {
        String role = service.check_role(userId);
        if(role.equals("tutee")){
            return "html/dashboard/myPage-tutees_Information";
        }else if(role.equals("tutor")){
            return "html/dashboard/myPage-tutors_Information";
        }
        return "html/dashboard/myPage-admin";
    }


    @GetMapping("/{user_id}/schedule")
    public String schedulePage(@PathVariable("user_id") String userId) {
        String role = service.check_role(userId);
        if(role.equals("tutee")) {
            return "html/dashboard/myPage-tutees_Schedule";
        }else if(role.equals("tutor")){
            return "html/dashboard/myPage-tutors_Schedule";
        }
        return "html/dashboard/myPage-admin";
    }

    @GetMapping("/{user_id}/history")
    public String historyPage() {
        return "html/dashboard/myPage-tutees_CourseHistory";
    }


    @RestController
    @RequestMapping("/mypage")
    public class MyPageRestController {

        @GetMapping("/admin/get")
        public String adminPage() {
            System.out.println("실행");
            List<MyPageDTO> list =service.view_admin();
            JSONArray jsonArray = new JSONArray();

            for (MyPageDTO dto : list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tutorName",dto.getName());
                jsonObject.put("approlDate",dto.getRegi_date());
                jsonObject.put("file",dto.getCertificate());

                jsonArray.add(jsonObject);
            }

            String jsonString = jsonArray.toString();
            System.out.println(jsonString);

            return jsonString;
        }

        @GetMapping("/{user_id}/get")
        public String myPage(@PathVariable("user_id") String userId) {
            MyPageDTO myPageDTO;
            JSONObject jsonObject = new JSONObject();
            String role = service.check_role(userId);
            if (role.equals("tutor")) {
                myPageDTO = service.view_tutor(userId);
                //System.out.println(tutorId);

                jsonObject.put("profileImg", myPageDTO.getProfileimg());
                jsonObject.put("name", myPageDTO.getName());
                jsonObject.put("email", myPageDTO.getEmail());
                jsonObject.put("nationality", myPageDTO.getNationality());
                jsonObject.put("interest1", myPageDTO.getInterest1());
                jsonObject.put("interest2", myPageDTO.getInterest2());
                jsonObject.put("genderRadios", myPageDTO.getGender());

            } else if(role.equals("tutee")) {
                myPageDTO = service.view_tutee(userId);
                System.out.println(userId);
//                System.out.println(myPageDTO);
                jsonObject.put("tutor-name", myPageDTO.getName());
                jsonObject.put("name", myPageDTO.getName());
                jsonObject.put("email", myPageDTO.getEmail());
                jsonObject.put("interest1", myPageDTO.getInterest1());
                jsonObject.put("interest2", myPageDTO.getInterest2());

            }

            System.out.println(jsonObject);
            return jsonObject.toJSONString();
        }

        @PostMapping("/{user_id}/post")
        public String myPageModify(@RequestBody MyPageDTO myPageDTO){
            String role = myPageDTO.getRole();
            if(role.equals("tutee")){
                service.modify_tutee(myPageDTO);
            }else if(role.equals("tutor")){
                service.modify_tutor(myPageDTO);
            }
            return null;
        }


        @GetMapping("/{user_id}/schedule/get")
        public String schedulePage() {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();


//            jsonObject.put("tutee_name", "Tom Softy");
//            jsonObject.put("user_id", "1234");
//
//            JSONArray schedules = new JSONArray();
//
//            JSONObject scheduleObject1 = new JSONObject();
//            scheduleObject1.put("class_date", "2023-04-16");
//            scheduleObject1.put("class_time", "10:00AM");
//            scheduleObject1.put("tutor_name", "Jonny Dep");
//            scheduleObject1.put("class_id", "202304161000");
//            schedules.add(scheduleObject1);
//
//            JSONObject scheduleObject2 = new JSONObject();
//            scheduleObject2.put("class_date", "2023-04-18");
//            scheduleObject2.put("class_time", "11:30AM");
//            scheduleObject2.put("tutor_name", "Angeli Remy");
//            scheduleObject2.put("class_id", "202304181130");
//            schedules.add(scheduleObject2);
//
//            jsonObject.put("schedules", schedules);
//            jsonArray.add(jsonObject);
//
            String jsonString = jsonArray.toString();
            System.out.println(jsonString);

            return jsonString;
        }

        @PutMapping("/{user_id}/schedule/cancel")
        public String scheduleCancel(@RequestBody JSONObject jsonObject) {
            String class_id = jsonObject.get("$class_id").toString();
            System.out.println(class_id);
            return class_id;
        }

        @GetMapping("/{user_id}/schedule/getCalendar")
        public String schedulePageCalendar(@PathVariable("user_id") String tutorId,
                                            @RequestParam("calendarDate") String calendarDate) {

            MypageScheduleDTO mypageScheduleDTO = new MypageScheduleDTO();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("tutorId",tutorId);
            paramMap.put("classDate", calendarDate);

            mypageScheduleDTO = service.schedule_tutor(paramMap, mypageScheduleDTO);

            System.out.println(mypageScheduleDTO.getUser_id());
            System.out.println(mypageScheduleDTO.getName());
            System.out.println(mypageScheduleDTO.getProfileimg());

            JSONObject jsonObject_info = new JSONObject();
            JSONArray jsonArray_schedule = new JSONArray();

            jsonObject_info.put("user_id",tutorId);
            jsonObject_info.put("name", mypageScheduleDTO.getName());
            jsonObject_info.put("picture", mypageScheduleDTO.getProfileimg());

            for(int i = 0; i<mypageScheduleDTO.getScheduleDTOList().size(); i++) {
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
            } catch (Exception e){
                e.printStackTrace();
            }
            if(result == 1 || result == 0)
                return "success";
            else
                return "fail";
        }

        @GetMapping("/{user_id}/history/get")
        public String historyPage() {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("class_date", "2023-04-16 10:00AM");
            obj1.put("tutor_name", "Jonny Dep");
            obj1.put("memo", "20200416.txt");
            jsonArray.add(obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("class_date", "2023-04-18 11:30AM");
            obj2.put("tutor_name", "Angeli Remy");
            obj2.put("memo", "20200418.txt");
            jsonArray.add(obj2);

            jsonObject.put("name", "김명진");
            jsonObject.put("user_id", "3125");
            jsonObject.put("history", jsonArray);

            String jsonString = jsonObject.toString();

            return jsonString;
        }
    }
}

