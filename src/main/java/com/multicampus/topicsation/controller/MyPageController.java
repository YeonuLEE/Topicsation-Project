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
        return "html/dashboard/myPage-tutors_Information";
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

        @GetMapping("/admin/get")
        public String adminPage(){
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
        public String myPage(){
            String jsonString = "{\"user_id\" : \"1234\",\"name\" : \"Tom hardy\",\"email\" : \"test@naver.com\",\"nationality\" : \"Europe\" ,\"interest1\" : \"fitness\",\"interest2\" : \"food\",\"genderRadios\" : \"male\",\"profileImg\" : \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==\",\"tutorName\" : \"DongHa\",\"withdrawal\" : \"\"}";
            return jsonString;

        }

        @GetMapping("/{user_id}/schedule.get")
        public void schedulePage(){

        }

        @GetMapping("/{user_id}/history.get")
        public void historyPage(){

        }

    }
}
