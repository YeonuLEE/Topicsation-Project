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

    @GetMapping("/{lesson_id}")
    public String lesson(){
        return "clasroom";
    }

    @RestController
    public class LessonRestController{

        @GetMapping("/{lesson_id}.get")
        public void lesson(){
        }


    }
}
