package com.multicampus.topicsation.controller;


import com.multicampus.topicsation.dto.MemberDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import com.multicampus.topicsation.service.ITutorListService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


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
//            MemberDTO memberDTO1 = new MemberDTO(
//                    "1001", "Yeonu Lee", "6.jpg",
//                    "320", "North America",
//                    "IT", "politics"
//            );
//            MemberDTO memberDTO2 = new MemberDTO(
//                    "1004", "Ahyeon Lee", "1004.jpg",
//                    "300", "Europe",
//                    "fitness", "food"
//            );
//            MemberDTO memberDTO3 = new MemberDTO(
//                    "1002", "Myeong Jin", "1002.jpg",
//                    "233", "Europe",
//                    "economics", "politics"
//            );
//            MemberDTO memberDTO4 = new MemberDTO(
//                    "1010", "Dongyoung Son", "unknown.png",
//                    "222", "South America",
//                    "economics", "IT"
//            );
//            MemberDTO memberDTO5 = new MemberDTO(
//                    "1011", "Dong-Ha Lee", "unknown.png",
//                    "220", "Asia",
//                    "economics", "food"
//            );
//            MemberDTO memberDTO6 = new MemberDTO(
//                    "1050", "Sumin Jeon", "6.jpg",
//                    "111", "Asia",
//                    "fitness", "IT"
//            );
//
//            List<MemberDTO> tutorList = new ArrayList<MemberDTO>();
//
//            tutorList.add(memberDTO1);
//            tutorList.add(memberDTO2);
//            tutorList.add(memberDTO3);
//            tutorList.add(memberDTO4);
//            tutorList.add(memberDTO5);
//            tutorList.add(memberDTO6);
//
//            JSONArray jsonArray = new JSONArray();
//            for (MemberDTO tutor : tutorList) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("user_id", tutor.getUser_id());
//                jsonObject.put("name", tutor.getName());
//                jsonObject.put("tutor_image", tutor.getTutor_image());
//                jsonObject.put("like", tutor.getLike());
//                jsonObject.put("nationality", tutor.getNationality());
//                jsonObject.put("interest1", tutor.getInterest1());
//                jsonObject.put("interest2", tutor.getInterest2());
//                jsonArray.add(jsonObject);
//            }
//            JSONObject jsonObject2 = new JSONObject();
//            jsonObject2.put("tutor_list", jsonArray);
//
//
//            String jsonString = jsonObject2.toJSONString();
//            System.out.println(jsonString);
//            return jsonString;
            return "";
        }

        @GetMapping("/search-all/search")
        public String search(@RequestParam String name,
                             @RequestParam String interest,
                             @RequestParam String date) {

            System.out.println("name : " + name);
            System.out.println("interest : " + interest);
            System.out.println("date : " + date);
//            MemberDTO memberDTO1 = new MemberDTO(
//                    "1001", "Yeonu Lee", "6.jpg",
//                    "320", "North America",
//                    "IT", "politics"
//            );
//            MemberDTO memberDTO2 = new MemberDTO(
//                    "1004", "Ahyeon Lee", "1004.jpg",
//                    "300", "Europe",
//                    "fitness", "food"
//            );
//            MemberDTO memberDTO3 = new MemberDTO(
//                    "1002", "Myeong Jin", "1002.jpg",
//                    "233", "Europe",
//                    "economics", "politics"
//            );
//            MemberDTO memberDTO4 = new MemberDTO(
//                    "1010", "Dongyoung Son", "unknown.png",
//                    "222", "South America",
//                    "economics", "IT"
//            );
//            MemberDTO memberDTO5 = new MemberDTO(
//                    "1011", "Dong-Ha Lee", "unknown.png",
//                    "220", "Asia",
//                    "economics", "food"
//            );
//            MemberDTO memberDTO6 = new MemberDTO(
//                    "1050", "Sumin Jeon", "6.jpg",
//                    "111", "Asia",
//                    "fitness", "IT"
//            );
//
//
//            List<MemberDTO> tutorList = new ArrayList<MemberDTO>();
//
//            tutorList.add(memberDTO1);
//            tutorList.add(memberDTO2);
//            tutorList.add(memberDTO3);
//            tutorList.add(memberDTO4);
//            tutorList.add(memberDTO5);
//            tutorList.add(memberDTO6);
//
//            JSONArray jsonArray = new JSONArray();
//            for (MemberDTO tutor : tutorList) {
//                JSONObject jsonObject = new JSONObject();
//                if (tutor.getInterest1().equals(interest) || tutor.getInterest2().equals(interest)) {
//                    jsonObject.put("user_id", tutor.getUser_id());
//                    jsonObject.put("name", tutor.getName());
//                    jsonObject.put("tutor_image", tutor.getTutor_image());
//                    jsonObject.put("like", tutor.getLike());
//                    jsonObject.put("nationality", tutor.getNationality());
//                    jsonObject.put("interest1", tutor.getInterest1());
//                    jsonObject.put("interest2", tutor.getInterest2());
//                    jsonArray.add(jsonObject);
//                }
//            }
//            JSONObject jsonObject2 = new JSONObject();
//            jsonObject2.put("tutor_list", jsonArray);
//
//
//            String jsonString = jsonObject2.toJSONString();
//            System.out.println(jsonString);
//            return jsonString;
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

            TutorViewDTO tutorViewDTO;
            tutorViewDTO = tutorListService.tutorInfo(paramMap);

            List<TutorScheduleDTO> classTimeList = new ArrayList<>();

            if (tutorViewDTO != null) {
                classTimeList = tutorListService.tutorSchedule(paramMap);
            }


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