package com.multicampus.topicsation.controller;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/members")
public class MemberManageController {

    @GetMapping("/admin")
    public String admin(){
        return "sign-in-admin";
    }

    @GetMapping("/signin")
    public String signin(){
        return "sign-in";
    }

    @GetMapping("/signin/find")
    public String passwordFind(){
        return "password-find";
    }

    @GetMapping("/signin/change")
    public String passwordChange(){
        return "password-change";
    }

    @GetMapping("/signup-tutees")
    public String signupTutees(){
        return "sign-up-tutee";
    }

    @GetMapping("/signup-tutors")
    public String signupTutors(){
        return "sign-up-tutor";
    }

    @GetMapping("/signup/email")
    public String emailAuth(){
        return "Email-Token";
    }

    @GetMapping("/signup/success")
    public String success(){
        return "sign-up-success";
    }

    @RestController
    public class MemberManageRestController{



    }
}
