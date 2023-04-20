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
        return "html/dashboard/myPage-tutors_Information";
    }

//    @GetMapping("/{user_id}/schedule")
//    public String schedulePage(){
//        return "html/dashboard/myPage-tutees_Information";
//    }

    @GetMapping("/{user_id}/schedule")
    public String schedulePage(){
        return "html/dashboard/myPage-tutors_Schedule";
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

//        @GetMapping("/{user_id}/schedule.get")
//        public void schedulePage(){
//
//        }

        @GetMapping("/{user_id}/schedule/getCalendar")
        public String schedulePage(@PathVariable("user_id") String tutorId,
            @RequestParam("classDate") String classDate){
            String jsonString = "{\n" +
                    "\"name\" : \"Michael Jackson\",\n" +
                    "\"profile_img\" : \"profile-picture-3.jpg\",\n" +
                    "\"schedule\" : \n" +
                    "[{\n" +
                    "\"class_id\" : \"5555\",\n" +
                    "\"class_date\" : \"2023-04-20\",\n" +
                    "\"class_time\" : \"0500PM\",\n" +
//                    "\"tutee_id\" : \"null\",\n" +
                    "\"tutee_name\" : \"null\",\n" +
                    "\"tutor_id\" : \"1234\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"class_id\" : \"7777\",\n" +
                    "\"class_date\" : \"2023-04-21\",\n" +
                    "\"class_time\" : \"0200PM\",\n" +
                    "\"tutee_id\" : \"1235\",\n" +
                    "\"tutee_name\" : \"김명진\",\n" +
                    "\"tutor_id\" : \"1234\"\n" +
                    "}]\n" +
                    "}";

            return jsonString;
        }

        @PostMapping("/{user_id}/schedule/postCalender")
        public String schedulePost(@RequestBody JSONObject jsonObject) {
            String tutor_id = jsonObject.get("$tutor_id").toString();
            System.out.println(tutor_id);
            return tutor_id;
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

            return jsonString;
        }

    }
}
