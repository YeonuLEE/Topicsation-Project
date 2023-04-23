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
        public String evaluateTutor(@RequestBody JSONObject jsonObject) {
            String result;
            String evaluate = jsonObject.get("$evaluate").toString();
            String lessonId = jsonObject.get("$lesson_id").toString();

            System.out.println("evaluate : " + evaluate);
            System.out.println("lessonId : " + lessonId);

            if (evaluate.equals("like")) {
                result = "like + 1";
                return result;
            } else {
                result = "dislike + 1";
                return result;
            }
        }

        @GetMapping("/{lesson_id}/getNews")
        public ResponseEntity<Object> getNews(@PathVariable String lesson_id) throws ParseException {

            System.out.println(lesson_id);
            JSONObject jsonObject = lessonService.getNewsService(lesson_id);

            return ResponseEntity.ok(jsonObject);
        }
    }
}
