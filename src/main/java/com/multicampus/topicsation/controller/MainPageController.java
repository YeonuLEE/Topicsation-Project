package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.RecommendDTO;
import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageRequestDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import com.multicampus.topicsation.service.ISearchService;
import org.json.simple.JSONArray;
import com.multicampus.topicsation.service.ITutorListService;
import com.multicampus.topicsation.service.SearchService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        ISearchService searchService;

        @GetMapping("/get")
        public String main(@RequestParam("userId") String userId) {
            return tutorListService.tutor_recommend(userId);
        }

        @GetMapping("/search-all/get")
        public ResponseEntity<Map<String, Object>> searchPage(@RequestParam Map<String, String> requestParams) {
            return searchService.searchList(requestParams);
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