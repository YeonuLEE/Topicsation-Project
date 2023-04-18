package com.multicampus.topicsation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/main")
public class MainPageController {

    @GetMapping("")
    public String main(){
        return "html/main";
    }

    @GetMapping("/search-all")
    public String searchAll(){
        return "html/main-search";
    }

    @GetMapping("/tutors/{tutor_id}")
    public String tutors(){
        return "Tutor-Detail-View";
    }


    @RestController
    public class MainPageRestController{

        @GetMapping(".get")
        public void main(){

        }

        @GetMapping("/search-all.get")
        public void searchAll(){

        }

        @GetMapping("/tutors/{tutor_id}.get")
        public void tutors(){

        }

    }
}
