package com.multicampus.topicsation.controller;


import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import com.multicampus.topicsation.service.ITutorListService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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

        @Autowired
        ITutorListService tutorListService;

        @GetMapping("/get")
        public String main() {
            String jsonString = "{\n" +
                    "    \"tutor_list\" : [\n" +
                    "        {\n" +
                    "            \"user_id\" : \"1001\",\n" +
                    "            \"name\" : \"Yeonu LEE\",\n" +
                    "            \"tutor_image\": \"6.jpg\",\n" +
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
           return "";
        }

        @GetMapping("/search-all/search")
        public String search(@RequestParam String name,
                             @RequestParam String interest,
                             @RequestParam String date) {
            return "";
        }


        @GetMapping("/tutors/{tutor_id}/getInfo")
        public String tutors(@PathVariable("tutor_id") String tutorId,
                             @RequestParam("calendarDate") String calendarDate) {

            System.out.println(tutorId);
            System.out.println(calendarDate);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("tutorId", tutorId);
            paramMap.put("classDate", calendarDate);

            TutorViewDTO tutorViewDTO = new TutorViewDTO();
            tutorViewDTO = tutorListService.tutorInfo(paramMap, tutorViewDTO);



            System.out.println(tutorViewDTO.getName());
            System.out.println(tutorViewDTO.getNationality());
            System.out.println(tutorViewDTO.getInfo());
            System.out.println(tutorViewDTO.getLike());
            System.out.println(tutorViewDTO.getProfileimg());
            System.out.println(tutorViewDTO.getInterest1());
            System.out.println(tutorViewDTO.getInterest2());

//            for(int i = 0; i<tutorViewDTO.getClass_time().size(); i++)
//                System.out.println(tutorViewDTO.getClass_time());

            JSONObject jsonObject_info = new JSONObject();
            JSONArray jsonArray_schedule = new JSONArray();
            JSONArray jsonArray_info = new JSONArray();
            jsonObject_info.put("user_id",tutorId);
            jsonObject_info.put("name", tutorViewDTO.getName());
            jsonObject_info.put("nationality", tutorViewDTO.getNationality());
            jsonObject_info.put("introduce", tutorViewDTO.getInfo());
            jsonObject_info.put("like", tutorViewDTO.getLike());
            jsonObject_info.put("picture", tutorViewDTO.getProfileimg());
            jsonObject_info.put("interest1", tutorViewDTO.getInterest1());
            jsonObject_info.put("interest2", tutorViewDTO.getInterest2());

            for(int i = 0; i<classTimeList.size(); i++) {
                JSONObject jsonObject_schedule = new JSONObject();
                jsonObject_schedule.put("class_id", classTimeList.get(i).getClass_id());
                jsonObject_schedule.put("class_date", classTimeList.get(i).getClass_date());
                jsonObject_schedule.put("class_time", classTimeList.get(i).getClass_time());
                jsonObject_schedule.put("tutee_id", classTimeList.get(i).getTutee_id());
                jsonObject_schedule.put("tutor_id", classTimeList.get(i).getTutor_id());
                jsonArray_schedule.add(jsonObject_schedule);
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("tutor_info", jsonObject_info);
            jsonObject.put("schedule", jsonArray_schedule);

            String jsonString = jsonObject.toJSONString();
            System.out.println(jsonString);

            return jsonString;
        }

        @PutMapping("/tutors/{tutor_id}/reservate")
        public String tutors(@RequestBody JSONObject jsonObject) {

            String tuteeId = jsonObject.get("$tutee_id").toString();
            String tutorId = jsonObject.get("$tutor_id").toString();
            String classDate = jsonObject.get("$class_date").toString();
            String classTime = jsonObject.get("$class_time").toString();

            System.out.println("tutee_id : " + tuteeId);
            System.out.println("tutor_id : " + tutorId);
            System.out.println("class_date : " + classDate);
            System.out.println("class_time : " + classTime);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("tutorId", tutorId);
            paramMap.put("tuteeId", tuteeId);
            paramMap.put("classDate", classDate);
            paramMap.put("classTime", classTime);

            boolean result_update;

            result_update = tutorListService.ClassReservate(paramMap);

            if(result_update == true)
                return "success";
            else
                return "fail";
        }
    }
}