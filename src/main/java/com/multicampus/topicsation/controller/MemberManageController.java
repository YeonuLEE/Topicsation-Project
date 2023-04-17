package com.multicampus.topicsation.controller;

import org.springframework.boot.Banner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/members")
public class MemberManageController {

    @GetMapping("/signin")
    public ModelAndView signin(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/sign-in");

        return modelAndView;
    }

    @GetMapping("/signin/find")
    public ModelAndView passwordFind(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/password-find");

        return modelAndView;
    }

    @GetMapping("/signin/emailAuth")
    public ModelAndView emailAuth(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/Email-Token");

        return modelAndView;
    }

    @GetMapping("/signin/change")
    public ModelAndView passwordChange(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/password-change");

        return modelAndView;
    }

    @GetMapping("/signup/tutees")
    public ModelAndView signupTutees(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/sign-up-tutee");

        return modelAndView;
    }

    @GetMapping("/signup/tutors")
    public ModelAndView signupTutors(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/sign-up-tutor");

        return modelAndView;
    }



}
