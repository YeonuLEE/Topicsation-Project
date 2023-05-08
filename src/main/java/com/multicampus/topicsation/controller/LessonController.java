package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.service.ILessonService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

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

        @PutMapping("/{lesson_id}/evaluate.do")
        public ResponseEntity<Void> evaluateTutor(@RequestBody Map<String,String> evaluateInfo) {
            if(lessonService.evaluateService(evaluateInfo) > 0){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.internalServerError().build();
            }
        }

        @PostMapping("/{lesson_id}/evaluate.review")
        public ResponseEntity<Void> reviewRegister(@RequestBody Map<String,String> reviewInfo) {
            if(lessonService.reviewService(reviewInfo) > 0){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.internalServerError().build();
            }
        }

        @GetMapping("/{lesson_id}/getNews")
        public ResponseEntity<Object> getNews(@PathVariable String lesson_id) throws ParseException {
            return ResponseEntity.ok(lessonService.getNewsService(lesson_id));
        }

        @GetMapping("/{lesson_id}/getMembers")
        public ResponseEntity<Object> getMembers(@PathVariable String lesson_id) {
            return ResponseEntity.ok(lessonService.getMembersService(lesson_id));
        }
    }
}
