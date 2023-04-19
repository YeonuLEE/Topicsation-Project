package com.multicampus.topicsation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        @GetMapping("/tutors/{tutor_id}/getInfo")
        public String tutors(@PathVariable("tutor_id") String tutorId,
                             @RequestParam("calendarDate") String calendarDate) {
            System.out.println(tutorId);
            System.out.println(calendarDate);
            String jsonString = "{\"schedule\" : [{\"class_id\" : \"5555\",\"class_date\" : \"2023-04-13\",\"class_time\" : \"0500PM\",\"tutee_id\" : \"null\",\"tutor_id\" : \"1234\"},{\"class_id\" : \"5556\",\"class_date\" : \"2023-04-13\",\"class_time\" : \"0530PM\",\"tutee_id\" : \"null\",\"tutor_id\" : \"1234\"}] ,\"tutor_info\":{\"user_id\" : \"1234\",\"name\" : \"Tomm hardy\",\"like\" : \"123\",\"nationality\" : \"europe\" ,\"interest1\" : \"fitness\",\"interest2\" : \"food\",\"introduce\" : \"안녕하세요~\",\"picture\" : \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==\"}}";
            return jsonString;
        }
    }
}