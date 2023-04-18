package com.multicampus.topicsation.controller;

import org.json.simple.JSONObject;
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
        return "html/Tutor-Detail-View";
    }




    @RestController
    @RequestMapping("/main")
    public class MainPageRestController{

        @GetMapping(".get")
        public void main(){

        }

        @GetMapping("/search-all.get")
        public void searchAll(){

        }

        @GetMapping("/tutors/{tutor_id}/get")
        public String tutors(){
            String jsonString = "{\"user_id\" : \"1234\",\"name\" : \"Tom hardy\",\"like\" : \"123\",\"nationality\" : \"europe\" ,\"interest1\" : \"fitness\",\"interest2\" : \"food\",\"introduce\" : \"안녕하세요~\",\"picture\" : \"image.jpg\"}";
            return jsonString;
        }

    }
}
