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

        @GetMapping("/{user_id}/history.get")
        public void historyPage(){

        }





    }
}
