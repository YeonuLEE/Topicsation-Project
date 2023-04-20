package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.MemberDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/main")
public class MainPageController {

    @GetMapping("")
    public String main() {
        return "html/main";
    }

    @GetMapping("/search-all")
    public String searchAll() {
        return "html/main-search";
    }

    @GetMapping("/tutors/{tutor_id}")
    public String tutors() {
        return "html/Tutor-Detail-View";
    }


    @RestController
    @RequestMapping("/main")
    public class MainPageRestController {

        @GetMapping("/get")
        public String main() {
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
        public String searchAll() {
            MemberDTO memberDTO1 = new MemberDTO(
                    "1001", "Yeonu Lee", "1001.jpg",
                    "320", "North America",
                    "IT", "politics"
            );
            MemberDTO memberDTO2 = new MemberDTO(
                    "1004", "Ahyeon Lee", "1004.jpg",
                    "300", "Europe",
                    "fitness", "food"
            );
            MemberDTO memberDTO3 = new MemberDTO(
                    "1002", "Myeong Jin", "1002.jpg",
                    "233", "Europe",
                    "economics", "politics"
            );
            MemberDTO memberDTO4 = new MemberDTO(
                    "1010", "Dongyoung Son", "unknown.png",
                    "222", "South America",
                    "economics", "IT"
            );
            MemberDTO memberDTO5 = new MemberDTO(
                    "1011", "Dong-Ha Lee", "unknown.png",
                    "220", "Asia",
                    "economics", "food"
            );
            MemberDTO memberDTO6 = new MemberDTO(
                    "1050", "Sumin Jeon", "1050.jpg",
                    "111", "Asia",
                    "fitness", "IT"
            );

            List<MemberDTO> tutorList = new ArrayList<MemberDTO>();

            tutorList.add(memberDTO1);
            tutorList.add(memberDTO2);
            tutorList.add(memberDTO3);
            tutorList.add(memberDTO4);
            tutorList.add(memberDTO5);
            tutorList.add(memberDTO6);

            JSONArray jsonArray = new JSONArray();
            for (MemberDTO tutor : tutorList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_id", tutor.getUser_id());
                jsonObject.put("name", tutor.getName());
                jsonObject.put("tutor_image", tutor.getTutor_image());
                jsonObject.put("like", tutor.getLike());
                jsonObject.put("nationality", tutor.getNationality());
                jsonObject.put("interest1", tutor.getInterest1());
                jsonObject.put("interest2", tutor.getInterest2());
                jsonArray.add(jsonObject);
            }
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("tutor_list",jsonArray);


            String jsonString = jsonObject2.toJSONString();
            System.out.println(jsonString);
            return jsonString;
        }

        @GetMapping("/search-all/search")
        public String search(@RequestParam String name,
                             @RequestParam String interest,
                             @RequestParam String date) {

            System.out.println("name : " + name);
            System.out.println("interest : " + interest);
            System.out.println("date : " + date);
            MemberDTO memberDTO1 = new MemberDTO(
                    "1001", "Yeonu Lee", "1001.jpg",
                    "320", "North America",
                    "IT", "politics"
            );
            MemberDTO memberDTO2 = new MemberDTO(
                    "1004", "Ahyeon Lee", "1004.jpg",
                    "300", "Europe",
                    "fitness", "food"
            );
            MemberDTO memberDTO3 = new MemberDTO(
                    "1002", "Myeong Jin", "1002.jpg",
                    "233", "Europe",
                    "economics", "politics"
            );
            MemberDTO memberDTO4 = new MemberDTO(
                    "1010", "Dongyoung Son", "unknown.png",
                    "222", "South America",
                    "economics", "IT"
            );
            MemberDTO memberDTO5 = new MemberDTO(
                    "1011", "Dong-Ha Lee", "unknown.png",
                    "220", "Asia",
                    "economics", "food"
            );
            MemberDTO memberDTO6 = new MemberDTO(
                    "1050", "Sumin Jeon", "1050.jpg",
                    "111", "Asia",
                    "fitness", "IT"
            );


                List<MemberDTO> tutorList = new ArrayList<MemberDTO>();

                tutorList.add(memberDTO1);
                tutorList.add(memberDTO2);
                tutorList.add(memberDTO3);
                tutorList.add(memberDTO4);
                tutorList.add(memberDTO5);
                tutorList.add(memberDTO6);

                JSONArray jsonArray = new JSONArray();
                for (MemberDTO tutor : tutorList) {
                    JSONObject jsonObject = new JSONObject();
                    if (tutor.getInterest1().equals(interest) || tutor.getInterest2().equals(interest)) {
                        jsonObject.put("user_id", tutor.getUser_id());
                        jsonObject.put("name", tutor.getName());
                        jsonObject.put("tutor_image", tutor.getTutor_image());
                        jsonObject.put("like", tutor.getLike());
                        jsonObject.put("nationality", tutor.getNationality());
                        jsonObject.put("interest1", tutor.getInterest1());
                        jsonObject.put("interest2", tutor.getInterest2());
                        jsonArray.add(jsonObject);
                    }
                }
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("tutor_list",jsonArray);


            String jsonString = jsonObject2.toJSONString();
            System.out.println(jsonString);
            return jsonString;
        }

        @GetMapping("/tutors/{tutor_id}/get")
        public String tutors() {
            String jsonString = "{\"user_id\" : \"1234\",\"name\" : \"Tom hardy\",\"like\" : \"123\",\"nationality\" : \"europe\" ,\"interest1\" : \"fitness\",\"interest2\" : \"food\",\"introduce\" : \"안녕하세요~\",\"picture\" : \"image.jpg\"}";
            return jsonString;
        }

    }
}
