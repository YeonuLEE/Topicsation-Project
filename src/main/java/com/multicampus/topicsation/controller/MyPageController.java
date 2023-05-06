package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.service.IMyPageService;
import com.multicampus.topicsation.service.IS3FileService;
import com.multicampus.topicsation.token.JwtUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private IMyPageService service;

    @Autowired
    private IS3FileService s3FileService;

    @Autowired
    private JwtUtils jwtUtils;


    @GetMapping("/tutee")
    public String tuteePage() {
        return "html/dashboard/myPage-tutees_Information";
    }

    @GetMapping("/tutor")
    public String tutorPage() {
        return "html/dashboard/myPage-tutors_Information";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "html/dashboard/myPage-admin";
    }

    @GetMapping("/tutee/schedule")
    public String tuteeSchedulePage() {
        return "html/dashboard/myPage-tutees_Schedule";
    }

    @GetMapping("/tutor/schedule")
    public String tutorSchedulePage() {
        return "html/dashboard/myPage-tutors_Schedule";
    }

    @GetMapping("/tutee/history")
    public String historyPage() {
        return "html/dashboard/myPage-tutees_CourseHistory";
    }


    @RestController
    @RequestMapping("/mypage")
    public class MyPageRestController {

        @GetMapping("/{user_id}")
        public String myPage(HttpServletRequest request) {
            return jwtUtils.authByRole(request, "/mypage/tutee", "/mypage/tutor", "/mypage/admin");
        }

        @GetMapping("/{user_id}/schedule")
        public String schedulePage(HttpServletRequest request) {
            return jwtUtils.authByRole(request, "/mypage/tutee/schedule", "/mypage/tutor/schedule");
        }

        @GetMapping("/{user_id}/history")
        public String historyPage(HttpServletRequest request) {
            return jwtUtils.authByRole(request, "/mypage/tutee/history");
        }

        @PostMapping("/admin/success")
        public String adminSuccess(@RequestBody String userId) {
            service.success(userId);
            return null;
        }

        @PostMapping("/admin/fail")
        public String adminFail(@RequestBody String userId) {
            service.fail(userId);
            return null;
        }

        @GetMapping("/download/{fileName}")
        public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) throws IOException {
            // 파일 경로를 수정하여 실제 파일의 위치를 지정해주세요.
            String filePath = "src/main/resources/static/assets/certificate/"+fileName;
            File file = new File(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }

        @GetMapping("/{user_id}/get")
        public String myPage(@PathVariable("user_id") String userId) {
            return service.view(userId);
        }

        @PostMapping("/{user_id}/passCheck")
        public boolean passCheck(@RequestBody Map<String, String> params, @PathVariable("user_id") String userId) {
            String password = params.get("password");
            String hashPass = service.check_password(userId);
            return BCrypt.checkpw(password, hashPass);
        }

        @PostMapping("/{user_id}/post")
        public String myPageModify(@RequestBody JSONObject jsonObject, @PathVariable("user_id") String userId) {
            service.modify(jsonObject,userId);
            return null;
        }

        @PostMapping("/{user_id}/profileUpdate")
        public ResponseEntity<?> mypageProfile(@PathVariable("user_id") String userId, @RequestParam("file") MultipartFile file){

            if(service.change_profileImg(userId, file)){
                return new ResponseEntity<>(file.getOriginalFilename() + " 파일이 업로드되었습니다.", HttpStatus.OK);// DB에 사진 파일 이름 저장
            }
            else{
                return new ResponseEntity<>(file.getOriginalFilename() + " 파일이 업로드되었습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }



        @PostMapping("/{user_id}/delete")
        public String myPageDelete(@PathVariable("user_id") String userId) {
            service.delete(userId);
            return null;
        }

        @GetMapping("/{user_id}/schedule/get")
        public String schedulePage(@PathVariable("user_id") String userId) {
            return service.schedule_tutee(userId);
        }

        @PutMapping("/{user_id}/schedule/cancel")
        public String scheduleCancel(@RequestBody JSONObject jsonObject) {
            service.schedule_cancel(jsonObject.get("$class_id").toString());
            return null;
        }

        @GetMapping("/{user_id}/schedule/getCalendar")
        public String schedulePageCalendar(@PathVariable("user_id") String tutorId,
                                           @RequestParam("calendarDate") String calendarDate) {

            MypageScheduleDTO mypageScheduleDTO = new MypageScheduleDTO();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("tutorId", tutorId);
            paramMap.put("classDate", calendarDate);

            mypageScheduleDTO = service.schedule_tutor(paramMap, mypageScheduleDTO);

            JSONObject jsonObject_info = new JSONObject();
            JSONArray jsonArray_schedule = new JSONArray();

            jsonObject_info.put("user_id", tutorId);
            jsonObject_info.put("name", mypageScheduleDTO.getName());
            jsonObject_info.put("picture", mypageScheduleDTO.getProfileimg());

            for (int i = 0; i < mypageScheduleDTO.getScheduleDTOList().size(); i++) {
                JSONObject jsonObject_schedule = new JSONObject();
                jsonObject_schedule.put("class_id", mypageScheduleDTO.getScheduleDTOList().get(i).getClass_id());
                jsonObject_schedule.put("class_date", mypageScheduleDTO.getScheduleDTOList().get(i).getClass_date());
                jsonObject_schedule.put("class_time", mypageScheduleDTO.getScheduleDTOList().get(i).getClass_time());
                jsonObject_schedule.put("tutee_id", mypageScheduleDTO.getScheduleDTOList().get(i).getTutee_id());
                jsonObject_schedule.put("tutor_id", mypageScheduleDTO.getScheduleDTOList().get(i).getTutor_id());
                jsonObject_schedule.put("name", mypageScheduleDTO.getScheduleDTOList().get(i).getName());
                jsonArray_schedule.add(jsonObject_schedule);
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("tutor_info", jsonObject_info);
            jsonObject.put("schedule", jsonArray_schedule);

            String jsonString = jsonObject.toJSONString();

            return jsonString;
        }

        @PostMapping("/{user_id}/schedule/postCalender")
        public String schedulePost(@RequestBody String jsonString) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            JSONObject jsonUserInfo;
            JSONArray jsonSchedule;
            int result = 0;
            try {
                jsonObject = (JSONObject) parser.parse(jsonString);
                jsonUserInfo = (JSONObject) jsonObject.get("user_info");
                jsonSchedule = (JSONArray) jsonObject.get("schedule");
                result = service.scheduleUpdate(jsonUserInfo, jsonSchedule);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result == 1 || result == 0)
                return "success";
            else
                return "fail";
        }

        @GetMapping("/{user_id}/history/get")
        public String historyPage(@PathVariable("user_id") String user_id) {
            return service.history_tutee(user_id);
        }
    }
}

