package com.multicampus.topicsation.controller;

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
            String jsonString = "{\"tno\" : \"1\",\"tutorName\" : \"Tom hardy\",\"approlDate\" : \"2023-04-15\",\"file\" : \"hero-1.jpg\"}";
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
