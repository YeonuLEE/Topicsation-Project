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

        @GetMapping("/get")
        public String main(){
            String jsonString = "{\n" +
                    "    \"tutor_list\" : [\n" +
                    "        {\n" +
                    "            \"user_id\" : \"1001\",\n" +
                    "            \"name\" : \"Yeonu LEE\",\n" +
                    "            \"tutor_image\": \"1001.jpg\",\n" +
                    "            \"like\" : \"320\",\n" +
                    "            \"nationality\" : \"North America\",\n" +
                    "            \"interest1\" : \"IT\",\n" +
                    "            \"interest2\" : \"Politics\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"user_id\" : \"1004\",\n" +
                    "            \"name\" : \"Ahyeon LEE\",\n" +
                    "            \"tutor_image\": \"1004.jpg\",\n" +
                    "            \"like\" : \"230\",\n" +
                    "            \"nationality\" : \"Europe\",\n" +
                    "            \"interest1\" : \"IT\",\n" +
                    "            \"interest2\" : \"Food\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"user_id\" : \"1002\",\n" +
                    "            \"name\" : \"Myeong Jin\",\n" +
                    "            \"tutor_image\": \"1002.jpg\",\n" +
                    "            \"like\" : \"200\",\n" +
                    "            \"nationality\" : \"Asia\",\n" +
                    "            \"interest1\" : \"IT\",\n" +
                    "            \"interest2\" : \"Fitness\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            return jsonString;
        }

        @GetMapping("/search-all.get")
        public String searchAll(){
            String jsonString = "{\n" +
                    "    \"tutor_list\" : [\n" +
                    "        {\n" +
                    "            \"user_id\" : \"1001\",\n" +
                    "            \"name\" : \"Yeonu LEE\",\n" +
                    "            \"tutor_image\": \"1001.jpg\",\n" +
                    "            \"like\" : \"320\",\n" +
                    "            \"nationality\" : \"North America\",\n" +
                    "            \"interest1\" : \"IT\",\n" +
                    "            \"interest2\" : \"Politics\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"user_id\" : \"1004\",\n" +
                    "            \"name\" : \"Ahyeon LEE\",\n" +
                    "            \"tutor_image\": \"1004.jpg\",\n" +
                    "            \"like\" : \"230\",\n" +
                    "            \"nationality\" : \"Europe\",\n" +
                    "            \"interest1\" : \"IT\",\n" +
                    "            \"interest2\" : \"Food\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"user_id\" : \"1002\",\n" +
                    "            \"name\" : \"Myeong Jin\",\n" +
                    "            \"tutor_image\": \"1002.jpg\",\n" +
                    "            \"like\" : \"200\",\n" +
                    "            \"nationality\" : \"Asia\",\n" +
                    "            \"interest1\" : \"IT\",\n" +
                    "            \"interest2\" : \"Fitness\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            return jsonString;
        }

        @GetMapping("/tutors/{tutor_id}/get")
        public String tutors(){
            String jsonString = "{\"user_id\" : \"1234\",\"name\" : \"Tom hardy\",\"like\" : \"123\",\"nationality\" : \"europe\" ,\"interest1\" : \"fitness\",\"interest2\" : \"food\",\"introduce\" : \"안녕하세요~\",\"picture\" : \"image.jpg\"}";
            return jsonString;
        }

    }
}
