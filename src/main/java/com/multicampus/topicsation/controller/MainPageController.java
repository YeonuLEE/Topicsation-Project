package com.multicampus.topicsation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/main")
public class MainPageController {

    @GetMapping("")
    public ModelAndView main(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/main");

        return modelAndView;
    }

    @GetMapping("/search-all")
    public ModelAndView mainSearch(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/main-search");

        return modelAndView;
    }

    @GetMapping("/tutors/{tutor_id}")
    public ModelAndView tutorSpec(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("html/Tutor-Detail-View");

        return modelAndView;
    }
}
