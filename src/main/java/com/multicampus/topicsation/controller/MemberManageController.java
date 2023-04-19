package com.multicampus.topicsation.controller;

import org.json.simple.JSONObject;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/members")
public class MemberManageController {

    @GetMapping("/admin")
    public String admin(){
        return "html/sign-in-admin";
    }

    @GetMapping("/signin")
    public String signin(){
        return "html/sign-in";
    }

    @GetMapping("/signin/find")
    public String passwordFind(){
        return "html/password-find";
    }

    @GetMapping("/signin/change")
    public String passwordChange(){
        return "html/password-change";
    }

    @GetMapping("/signup-tutees")
    public String signupTutees(){
        return "html/sign-up-tutee";
    }

    @GetMapping("/signup-tutors")
    public String signupTutors(){
        return "html/sign-up-tutor";
    }

    @GetMapping("/signup/email")
    public String emailAuth(){
        return "html/Email-Token";
    }

    @GetMapping("/signup/success")
    public String success(){
        return "html/sign-up-success";
    }

    @RestController
    @RequestMapping("/members")
    public class MemberManageRestController{

        @PostMapping("/email.auth")
        public String emailAuth(@RequestBody JSONObject jsonObject){
            System.out.println(jsonObject.get("test"));
            return "success";
        }



    }
}
