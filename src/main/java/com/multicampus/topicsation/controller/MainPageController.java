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

            Map<String, Object> paramMap = new HashMap<>(); //tutor_id 와 calendarDate 를 담을 부분

            paramMap.put("tutorId", tutorId);
            paramMap.put("classDate", calendarDate);

            TutorViewDTO tutorViewDTO = new TutorViewDTO(); //Service로 보낼 DTO 생성

            tutorViewDTO = tutorListService.tutorInfo(paramMap, tutorViewDTO);
            // Service에 paramMap과 DTO를 보내서 불러온 DTO 반환

//            System.out.println(tutorViewDTO.getName());
//            System.out.println(tutorViewDTO.getNationality());
//            System.out.println(tutorViewDTO.getInfo());
//            System.out.println(tutorViewDTO.getLike());
//            System.out.println(tutorViewDTO.getProfileimg());
//            System.out.println(tutorViewDTO.getInterest1());
//            System.out.println(tutorViewDTO.getInterest2());
//            System.out.println();

            JSONObject jsonObject_info = new JSONObject(); //JSON의 info부분
            JSONArray jsonArray_schedule = new JSONArray(); //JSON의 schedule부분 (schedule이 List이기 때문에 Array로 선언

            jsonObject_info.put("user_id",tutorId);
            jsonObject_info.put("name", tutorViewDTO.getName());
            jsonObject_info.put("nationality", tutorViewDTO.getNationality());
            jsonObject_info.put("introduce", tutorViewDTO.getInfo());
            jsonObject_info.put("like", tutorViewDTO.getLike());
            jsonObject_info.put("picture", tutorViewDTO.getProfileimg());
            jsonObject_info.put("interest1", tutorViewDTO.getInterest1());
            jsonObject_info.put("interest2", tutorViewDTO.getInterest2());
            //JSON에 info 부분 put

            for(int i = 0; i<tutorViewDTO.getClassTimeList().size(); i++) {
                JSONObject jsonObject_schedule = new JSONObject();
                jsonObject_schedule.put("class_id", tutorViewDTO.getClassTimeList().get(i).getClass_id());
                jsonObject_schedule.put("class_date", tutorViewDTO.getClassTimeList().get(i).getClass_date());
                jsonObject_schedule.put("class_time", tutorViewDTO.getClassTimeList().get(i).getClass_time());
                jsonObject_schedule.put("tutee_id", tutorViewDTO.getClassTimeList().get(i).getTutee_id());
                jsonObject_schedule.put("tutor_id", tutorViewDTO.getClassTimeList().get(i).getTutor_id());
                jsonArray_schedule.add(jsonObject_schedule);
            }
            //List의 처음부터 끝까지 순회하여 각각의 jsonObject를 생성해서 jsonArray에 add

            JSONObject jsonObject = new JSONObject(); //jsonInfo와 jsonSchedule을 담아줄 json생성

            jsonObject.put("tutor_info", jsonObject_info);
            jsonObject.put("schedule", jsonArray_schedule);

            String jsonString = jsonObject.toJSONString(); //response로 보내기 위해 String화
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