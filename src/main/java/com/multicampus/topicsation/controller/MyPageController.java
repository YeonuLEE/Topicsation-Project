package com.multicampus.topicsation.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return "html/dashboard/myPage-tutees_Information";
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

        @GetMapping("/{user_id}.get")
        public void myPage(){

        }

        @GetMapping("/{user_id}/schedule.get")
        public void schedulePage(){

        }

        @GetMapping("/{user_id}/history/get")
        public String historyPage(){
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

            jsonObject.put("name","김명진");
            jsonObject.put("user_id","3125");
            jsonObject.put("history",jsonArray);

            String jsonString = jsonObject.toString();
            System.out.println(jsonString);

            return jsonString;
        }





    }
}
