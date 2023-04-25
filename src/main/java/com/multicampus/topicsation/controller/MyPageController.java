package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
import com.multicampus.topicsation.service.IMyPageService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            JSONArray jsonArray = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("tutorName", "Jonny Dep");
            obj1.put("approlDate", "2023-04-16 10:00AM");
            obj1.put("file", "20200416.pdf");
            jsonArray.add(obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("tutorName", "Angeli Remy");
            obj2.put("approlDate", "2023-04-18 11:30AM");
            obj2.put("file", "20200418.pdf");
            jsonArray.add(obj2);

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
        public String schedulePage(@PathVariable("user_id") String user_id) {

            MypageScheduleDTO profileDto = service.tuteeProfile(user_id);
            List<ClassDTO> scheduleDTOList = service.schedule_tutee(user_id);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("user_id",profileDto.getUser_id());
            jsonObject.put("tutee_name",profileDto.getName());


            for(ClassDTO dto : scheduleDTOList){
                JSONObject jsonObject2 =new JSONObject();
                jsonObject2.put("class_id",dto.getClass_id());
                jsonObject2.put("class_date",dto.getClass_date());
                jsonObject2.put("class_time",dto.getClass_time());
                jsonObject2.put("tutee_id",dto.getTutee_id());
                jsonObject2.put("tutor_name",dto.getName());
                jsonObject2.put("tutor_id",dto.getTutor_id());

                jsonArray.add(jsonObject2);
            }
            jsonObject.put("schedules",jsonArray);
            String jsonString = jsonObject.toString();
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
        public String schedulePageCalendar(@PathVariable("user_id") String tutorId) {
            MypageScheduleDTO profileDto = service.tutorProfile(tutorId);
            List<ClassDTO> scheduleDTOList = service.schedule_tutor(tutorId);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tutor_id",profileDto.getUser_id());
            jsonObject.put("name",profileDto.getName());
            jsonObject.put("profileimg",profileDto.getProfileimg());

            JSONArray jsonArray = new JSONArray();
            for(ClassDTO dto : scheduleDTOList){
                JSONObject jsonObject2 =new JSONObject();
                jsonObject2.put("class_id",dto.getClass_id());
                jsonObject2.put("class_date",dto.getClass_date());
                jsonObject2.put("class_time",dto.getClass_time());
                jsonObject2.put("tutee_id",dto.getTutee_id());
                jsonObject2.put("tutee_name",dto.getName());
                jsonObject2.put("tutor_id",dto.getTutor_id());

                jsonArray.add(jsonObject2);
            }

            jsonObject.put("schedule",jsonArray);
            String jsonString = jsonObject.toJSONString();

            return jsonString;
        }

        @PostMapping("/{user_id}/schedule/postCalender")
        public String schedulePost(@RequestBody JSONObject jsonObject) {
            String tutor_id = jsonObject.get("$tutor_id").toString();
            System.out.println(tutor_id);
            return tutor_id;
        }

        @GetMapping("/{user_id}/history/get")
        public String historyPage(@PathVariable("user_id") String user_id) {
            MypageScheduleDTO profileDto = service.tuteeProfile(user_id);
            List<ClassDTO> scheduleDTOList = service.history_tutee(user_id);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name",profileDto.getName());


            for(ClassDTO dto : scheduleDTOList){
                JSONObject jsonObject2 =new JSONObject();
                jsonObject2.put("class_date",dto.getClass_date());
                jsonObject2.put("class_time",dto.getClass_time());
                jsonObject2.put("tutor_name",dto.getName());

                jsonArray.add(jsonObject2);
            }
            jsonObject.put("history",jsonArray);
            String jsonString = jsonObject.toString();
//            System.out.println(jsonString);

            return jsonString;
        }
    }
}

