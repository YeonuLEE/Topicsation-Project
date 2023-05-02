package com.multicampus.topicsation.controller;


import com.multicampus.topicsation.dto.RecommendDTO;
import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import com.multicampus.topicsation.service.ITutorListService;
import com.multicampus.topicsation.service.SearchService;
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

        @Autowired
        SearchService searchService;

        @GetMapping("/get")
        public String main() {
            String userId = "";
            List<RecommendDTO> list;
            if(!userId.isEmpty() && userId.equals("")){
                list = tutorListService.recommend(userId);
            }else {
                list = tutorListService.Non_members();
            }
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            for(RecommendDTO dto : list){
                JSONObject object = new JSONObject();
                object.put("user_id",dto.getUser_id());
                object.put("name",dto.getName());
                object.put("tutor_image",dto.getProfileImg());
                object.put("like",dto.getLike());
                object.put("nationality",dto.getNationality());
                object.put("interest1",dto.getInterest1());
                object.put("interest2",dto.getInterest2());

                jsonArray.add(object);
            }

            jsonObject.put("tutor_list",jsonArray);
            String jsonString = jsonObject.toJSONString();

            return jsonString;
        }
        @GetMapping("/search-all.get")
        public String searchAll(@RequestParam("currentPage")int currentPage, @RequestParam("dataPerPage") int dataPerPage) {
            PageReqeustDTO pageReqeustDTO = new PageReqeustDTO();
            pageReqeustDTO.setCurrentPage(currentPage);
            pageReqeustDTO.setDataPerPage(dataPerPage);
            pageReqeustDTO.setStartData((currentPage - 1) * 6 + 1);
            List<SearchDTO> searchAll = searchService.allList(pageReqeustDTO);

            for(int i = 0; i<searchAll.size(); i++){
                System.out.println(searchAll.get(i));
            }


            int totalDataCount = searchService.searchCount(pageReqeustDTO);
            PageResponseDTO pageResponseDTO = new PageResponseDTO(pageReqeustDTO, totalDataCount);

            JSONArray listArray = new JSONArray();
            JSONArray pagingArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            for(SearchDTO dto : searchAll){
                JSONObject object = new JSONObject();
                object.put("user_id", dto.getUser_id());
                object.put("name",dto.getName());
                object.put("tutor_image",dto.getProfileImg());
                object.put("like",dto.getLike());
                object.put("nationality",dto.getNationality());
                object.put("interest1",dto.getInterest1());
                object.put("interest2",dto.getInterest2());
                listArray.add(object);
            }
            jsonObject.put("all_list", listArray);

            JSONObject pagingObject = new JSONObject();
            pagingObject.put("currentPage", pageResponseDTO.getCurrentPage());
            pagingObject.put("dataPerPage", pageResponseDTO.getDataPerPage());
            pagingObject.put("pagePerOnce", pageResponseDTO.getPagePerOnce());
            pagingObject.put("totalDataCount", totalDataCount);
            pagingObject.put("totalPageCount", pageResponseDTO.getTotalPageCount());
            pagingObject.put("startData", pageResponseDTO.getStartData());
            pagingObject.put("startPage", pageResponseDTO.getStartPage());
            pagingObject.put("endPage", pageResponseDTO.getEndPage());
            pagingObject.put("prev", pageResponseDTO.isPrev());
            pagingObject.put("next", pageResponseDTO.isNext());
            pagingArray.add(pagingObject);

            jsonObject.put("paging",pagingArray);

            String jsonString = jsonObject.toJSONString();
            System.out.println("[controller jsonstring] \n" + jsonString);

           return jsonString;
        }


        @GetMapping("/search-all/search")
        public String search(@RequestParam("name") String name, @RequestParam("interest") String interest, @RequestParam("classDate") String classDate,
                             @RequestParam("currentPage") int currentPage, @RequestParam("dataPerPage") int dataPerPage) {
            PageReqeustDTO pageReqeustDTO = new PageReqeustDTO();
            pageReqeustDTO.setName(name);
            pageReqeustDTO.setInterest(interest);
            pageReqeustDTO.setClassDate(classDate);
            pageReqeustDTO.setCurrentPage(currentPage);
            pageReqeustDTO.setDataPerPage(dataPerPage);
            List<SearchDTO> searchAll = searchService.searchList(pageReqeustDTO);
            int totalDataCount = searchService.searchCount(pageReqeustDTO);
            PageResponseDTO pageResponseDTO = new PageResponseDTO(pageReqeustDTO, totalDataCount);

            JSONArray listArray = new JSONArray();
            JSONArray pagingArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            for(SearchDTO dto : searchAll){
                JSONObject object = new JSONObject();
                object.put("user_id", dto.getUser_id());
                object.put("name",dto.getName());
                object.put("tutor_image",dto.getProfileImg());
                object.put("like",dto.getLike());
                object.put("nationality",dto.getNationality());
                object.put("interest1",dto.getInterest1());
                object.put("interest2",dto.getInterest2());
                listArray.add(object);
            }
            jsonObject.put("search_list", listArray);

            JSONObject pagingObject = new JSONObject();
            pagingObject.put("currentPage", pageResponseDTO.getCurrentPage());
            pagingObject.put("dataPerPage", pageResponseDTO.getDataPerPage());
            pagingObject.put("pagePerOnce", pageResponseDTO.getPagePerOnce());
            pagingObject.put("totalDataCount", totalDataCount);
            pagingObject.put("totalPageCount", pageResponseDTO.getTotalPageCount());
            pagingObject.put("startData", pageResponseDTO.getStartData());
            pagingObject.put("startPage", pageResponseDTO.getStartPage());
            pagingObject.put("endPage", pageResponseDTO.getEndPage());
            pagingObject.put("prev", pageResponseDTO.isPrev());
            pagingObject.put("next", pageResponseDTO.isNext());
            pagingArray.add(pagingObject);

            jsonObject.put("paging",pagingArray);

            String jsonString = jsonObject.toJSONString();
            System.out.println("[controller jsonstring] \n" + jsonString);

            return jsonString;
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

            JSONObject jsonObject_info = new JSONObject();
            JSONArray jsonArray_schedule = new JSONArray();
            JSONArray jsonArray_Review = new JSONArray();

            jsonObject_info.put("user_id",tutorId);
            jsonObject_info.put("name", tutorViewDTO.getName());
            jsonObject_info.put("nationality", tutorViewDTO.getNationality());
            jsonObject_info.put("introduce", tutorViewDTO.getInfo());
            jsonObject_info.put("like", tutorViewDTO.getLike());
            jsonObject_info.put("picture", tutorViewDTO.getProfileimg());
            jsonObject_info.put("interest1", tutorViewDTO.getInterest1());
            jsonObject_info.put("interest2", tutorViewDTO.getInterest2());

            for(int i = 0; i<tutorViewDTO.getClassTimeList().size(); i++) {
                JSONObject jsonObject_schedule = new JSONObject();
                jsonObject_schedule.put("class_id", tutorViewDTO.getClassTimeList().get(i).getClass_id());
                jsonObject_schedule.put("class_date", tutorViewDTO.getClassTimeList().get(i).getClass_date());
                jsonObject_schedule.put("class_time", tutorViewDTO.getClassTimeList().get(i).getClass_time());
                jsonObject_schedule.put("tutee_id", tutorViewDTO.getClassTimeList().get(i).getTutee_id());
                jsonObject_schedule.put("tutor_id", tutorViewDTO.getClassTimeList().get(i).getTutor_id());
                jsonArray_schedule.add(jsonObject_schedule);
            }

            for(int i = 0; i<tutorViewDTO.getTutorReviewList().size(); i++){
                JSONObject jsonObject_Review = new JSONObject();
                jsonObject_Review.put("tutee_name", tutorViewDTO.getTutorReviewList().get(i).getName());
                jsonObject_Review.put("review_date", tutorViewDTO.getTutorReviewList().get(i).getReviewDate());
                jsonObject_Review.put("review_content", tutorViewDTO.getTutorReviewList().get(i).getReviewContent());
                jsonArray_Review.add(jsonObject_Review);
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("tutor_info", jsonObject_info);
            jsonObject.put("schedule", jsonArray_schedule);
            jsonObject.put("review",jsonArray_Review);

            String jsonString = jsonObject.toJSONString();
            System.out.println(jsonString);

            return jsonString;
        }

        @PutMapping("/tutors/{tutor_id}/reserve")
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

            result_update = tutorListService.ClassReserve(paramMap);

            if(result_update == true)
                return "success";
            else
                return "fail";
        }
    }
}