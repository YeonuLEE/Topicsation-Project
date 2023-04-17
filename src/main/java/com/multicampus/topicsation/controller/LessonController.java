package com.multicampus.topicsation.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    @GetMapping("/{lesson_id}") // URI 패턴
    public ModelAndView lesson() {
        ModelAndView modelAndView = new ModelAndView("html/classroom"); // 실제 경로
        //modelAndView.setViewName("ex/classroom");
        //modelAndView.addObject() 기존에 했던 방식과 동일한 방식

        return modelAndView;
    }

    @PostMapping("/classroom.action")
    public String getNews(HttpServletRequest req) throws Exception{
        String stringNews = req.getParameter("testNews1");

        return stringNews;
    }
}
