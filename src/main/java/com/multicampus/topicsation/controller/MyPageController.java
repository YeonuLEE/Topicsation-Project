package com.multicampus.topicsation.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @GetMapping("/admin")
    public String adminPage(){
        return "html/dashboard/myPage-admin";
    }

    @GetMapping("/{user_id}")
    public String myPage(){
        return "html/dashboard/myPage-tutees_Information";
    }

    @GetMapping("/{user_id}/schedule")
    public String schedulePage(){
        return "html/dashboard/myPage-tutees_Schedule";
    }

    @GetMapping("/{user_id}/history")
    public String historyPage(){
        return "html/dashboard/myPage-tutees_CourseHistory";
    }


    @RestController
    @RequestMapping("/mypage")
    public class MyPageRestController{

        @GetMapping("/admin.get")
        public void adminPage(){

        }

        @GetMapping("/{user_id}/get")
        public String myPage(){
            String jsonString = "{\"user_id\" : \"1234\",\"name\" : \"Tom softy\",\"email\" : \"hardybrother@gmail.com\",\"interest1\" : \"fitness\",\"interest2\" : \"food\",\"password\" : \"1234\"}";
            return jsonString;
        }


        @GetMapping("/{user_id}/schedule/get")
        public String schedulePage(){
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tutee_name", "Tom Softy");
            jsonObject.put("user_id", "1234");

            JSONArray schedules = new JSONArray();

            JSONObject scheduleObject1 = new JSONObject();
            scheduleObject1.put("class_date", "2023-04-16");
            scheduleObject1.put("class_time", "10:00AM");
            scheduleObject1.put("tutor_name", "Jonny Dep");
            scheduleObject1.put("class_id", "202304161000");
            schedules.add(scheduleObject1);

            JSONObject scheduleObject2 = new JSONObject();
            scheduleObject2.put("class_date", "2023-04-18");
            scheduleObject2.put("class_time", "11:30AM");
            scheduleObject2.put("tutor_name", "Angeli Remy");
            scheduleObject2.put("class_id", "202304181130");
            schedules.add(scheduleObject2);

            jsonObject.put("schedules", schedules);
            jsonArray.add(jsonObject);

            String jsonString = jsonArray.toString();
            System.out.println(jsonString);

            return jsonString;
        }
        @PutMapping("/{user_id}/schedule/cancel")
        public String scheduleCancel(@RequestBody JSONObject jsonObject){
            String class_id = jsonObject.get("$class_id").toString();
            System.out.println(class_id);
            return class_id;
        }

        @GetMapping("/{user_id}/history.get")
        public void historyPage(){

        }
    }
}
