package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageRequestDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import com.multicampus.topicsation.service.ITutorListService;
import com.multicampus.topicsation.service.SearchService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        public String main(@RequestParam("userId") String userId) {
            return tutorListService.tutor_recommend(userId);
        }

        @GetMapping("/search-all/get")
        public ResponseEntity<Map<String, Object>> searchPage(@RequestParam Map<String, String> requestParams) {
            int page = Integer.parseInt(requestParams.getOrDefault("page", "1"));
            int size = Integer.parseInt(requestParams.getOrDefault("size", "6"));
            String name = requestParams.get("name");
            String interest = requestParams.get("interest");
            String date = requestParams.get("date");

            if(date != null){
                SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date dateFormat = inputFormat.parse(date);
                    date = outputFormat.format(dateFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                    .name(name)
                    .interest(interest)
                    .date(date)
                    .page(page)
                    .size(size)
                    .build();

            PageResponseDTO<SearchDTO> pageResponseDTO = searchService.searchList(pageRequestDTO);

            Map <String, Object> resultMap = new HashMap<>();
            resultMap.put("all_list",pageResponseDTO.getSearchDTOList());
            resultMap.put("page",pageResponseDTO.getPage());
            resultMap.put("size",pageResponseDTO.getSize());
            resultMap.put("total",pageResponseDTO.getTotal());
            resultMap.put("start",pageResponseDTO.getStart());
            resultMap.put("end",pageResponseDTO.getEnd());
            resultMap.put("prev",pageResponseDTO.isPrev());
            resultMap.put("next",pageResponseDTO.isNext());

           return new ResponseEntity<Map<String, Object>> (resultMap, HttpStatus.OK);
        }

        @GetMapping("/tutors/{tutor_id}/getInfo")
        public String tutors(@PathVariable("tutor_id") String tutorId,
                             @RequestParam("calendarDate") String calendarDate) {
            return tutorListService.tutorInfo(tutorId,calendarDate);
        }

        @PutMapping("/tutors/{tutor_id}/reserve")
        public ResponseEntity<Void> tutors(@RequestBody JSONObject jsonObject) {
            return tutorListService.ClassReserve(jsonObject);
        }
    }
}