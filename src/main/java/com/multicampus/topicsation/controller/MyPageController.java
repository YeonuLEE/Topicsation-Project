package com.multicampus.topicsation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    @GetMapping("/{member_id}")
    public ModelAndView memberInfo(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/dashboard/myPage-tutees_Information");

        return modelAndView;
    }

    @GetMapping("/{member_id}/schedule")
    public ModelAndView memberSchedule(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/dashboard/myPage-tutees_Schedule");

        return modelAndView;
    }

    @GetMapping("/{member_id}/history")
    public ModelAndView memberHistory(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/dashboard/myPage-tutees_CourseHistory");

        return modelAndView;
    }
}
