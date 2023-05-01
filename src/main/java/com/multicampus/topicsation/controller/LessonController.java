package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.service.ILessonService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    ILessonService lessonService; // injection 수정 고려해볼 것

    @GetMapping("/{lesson_id}")
    public String lesson() {
        return "html/classroom";
    }

    @GetMapping("/{lesson_id}/evaluate")
    public String evaluate() {
        return "html/evaluate-popup";
    }

    @RestController
    @RequestMapping("/lesson")
    public class LessonRestController {

        @PostMapping("/{lesson_id}/newsView")
        public String lesson(@RequestBody JSONObject jsonObject) {
            String news = jsonObject.get("$testNews1").toString();
            return news;
        }

        @PutMapping("/{lesson_id}/evaluate.do")
        public int evaluateTutor(@RequestBody JSONObject jsonObject) {

            String evaluate = jsonObject.get("$evaluate").toString();
            String lessonId = jsonObject.get("$lesson_id").toString();

            return lessonService.evaluateService(evaluate, lessonId);
        }

        @PostMapping("/{lesson_id}/evaluate.review")
        public int reviewRegister(@RequestBody JSONObject jsonObject) {

            String review_content = jsonObject.get("$review_content").toString();
            String lessonId = jsonObject.get("$lesson_id").toString();

            return lessonService.reviewService(review_content, lessonId);
        }

        @GetMapping("/{lesson_id}/getNews")
        public ResponseEntity<Object> getNews(@PathVariable String lesson_id) throws ParseException {

            System.out.println("레슨 아이디 : " + lesson_id);
            JSONObject jsonObject = lessonService.getNewsService(lesson_id);

            return ResponseEntity.ok(jsonObject);
        }


    }
}
