package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.MemberRole;
import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.service.SignUpService;
import org.json.simple.JSONObject;
import org.springframework.boot.Banner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/members")
public class MemberManageController {

    public SignUpService signUpService;

    @GetMapping("/admin")
    public String admin() {
        return "html/sign-in-admin";
    }

    @GetMapping("/signin")
    public String signin() {
        return "html/sign-in";
    }

    @GetMapping("/signin/find")
    public String passwordFind() {
        return "html/password-find";
    }

    @GetMapping("/signin/change")
    public String passwordChange() {
        return "html/password-change";
    }

    @GetMapping("/signup-tutees")
    public String signupTutees() throws Exception {
        System.out.println("signupTutees 회원가입 페이지 이동");
        return "html/sign-up-tutee";
    }

    @GetMapping("/signup-tutors")
    public String signupTutors() throws Exception{
        System.out.println("signupTutors 회원가입 페이지 이동");
        return "html/sign-up-tutor";
    }

    @GetMapping("/signup/email")
    public String emailAuth() throws Exception {
        System.out.println("회원가입 폼 입력 후 이메일 인증 페이지 이동");
        return "html/Email-Token";
    }

    @GetMapping("/signup/success")
    public String success() {
        System.out.println("회원가입 성공 페이지 이동");
        return "html/sign-up-success";
    }



    @RestController
    @RequestMapping("/members")
    public class MemberManageRestController {


        // 회원가입
        @PostMapping("/signup-tutees.post")
        public boolean signUpTutee(@RequestBody Map<String, String> jsonMap) {

            String email = jsonMap.get("$email").toString();
            String password = jsonMap.get("$password").toString();
            String name = jsonMap.get("$name").toString();
            String firstInterest = jsonMap.get("$firstInterest").toString();
            String secondInterest = jsonMap.get("$secondInterest").toString();


            if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                    || firstInterest.isEmpty() || secondInterest.isEmpty()) {
                System.out.println("signup Fail");
                return false;
            }

            SignUpDTO dto = new SignUpDTO();
            dto.setEmail(jsonMap.get("$email").toString());
            dto.setPassword(jsonMap.get("$password").toString());
            dto.setName(jsonMap.get("$name").toString());
            dto.setInterest1(jsonMap.get("$firstInterest").toString());
            dto.setInterest2(jsonMap.get("$secondInterest").toString());
            dto.setRole(MemberRole.TUTEE);

            signUpService.addTutee(dto);
            System.out.println(dto);

            return true;


//        @PostMapping("/signup-tutees.post")
//        public String signUpTutee(@RequestBody JSONObject jsonObject) {
//            String result;
//            String email = jsonObject.get("$email").toString();
//            String password = jsonObject.get("$password").toString();
//            String name = jsonObject.get("$name").toString();
//            String firstInterest = jsonObject.get("$firstInterest").toString();
//            String secondInterest = jsonObject.get("$secondInterest").toString();
//
//            System.out.println(email);
//            System.out.println(password);
//            System.out.println(name);
//            System.out.println(firstInterest);
//            System.out.println(secondInterest);
//
//            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()
//                    && !firstInterest.isEmpty() && !secondInterest.isEmpty()) {
//                result = "signupSuccess";
//            } else {
//                result = "signupFail";
//            }
//            System.out.println(result);
//
//            return result;
        }

        //        @RequestMapping(value = "/signup-tutors.post", method = RequestMethod.POST)
        @PostMapping("/signup-tutors.post")
        public String signUpTutor(@RequestBody JSONObject jsonObject) {
            String result;
            String email = jsonObject.get("$email").toString();
            String password = jsonObject.get("$password").toString();
            String name = jsonObject.get("$name").toString();
            String gender = jsonObject.get("$gender").toString();
            String nationality = jsonObject.get("$nationality").toString();
            String firstInterest = jsonObject.get("$firstInterest").toString();
            String secondInterest = jsonObject.get("$secondInterest").toString();
            //String formData = jsonObject.get("$formData").toString();

            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !nationality.isEmpty()
                    && !firstInterest.isEmpty() && !secondInterest.isEmpty() && !gender.isEmpty()) {
                result = "success";
            } else {
                result = "fail";
            }
            System.out.println("result : " + result);
            return result;
        }

        @PostMapping("/email.auth")
        public String emailAuth(@RequestBody JSONObject jsonObject) {
            System.out.println(jsonObject.get("test"));
            return "success";
        }

        @PostMapping("/signin/change.post")
        public String passwordChange(@RequestBody JSONObject jsonObject) {
            String password = jsonObject.get("$password").toString();
            String confirmPassword = jsonObject.get("$confirmPassword").toString();

            System.out.println(password);
            System.out.println(confirmPassword);

            return "test!!";
        }

        @PostMapping("/singin/find/post")
        public String passwordFind(@RequestBody JSONObject jsonObject) {
            String email = jsonObject.get("$email").toString();
            String user_id = jsonObject.get("$user_id").toString();
            System.out.println(email);
            System.out.println(user_id);
            System.out.println("please");
            return email;
        }



        @PostMapping("/signin.post")
        public String signin(@RequestBody JSONObject jsonObject) {
            String result;

            String email = jsonObject.get("$email").toString();
            String password = jsonObject.get("$password").toString();

            if (email.equals("AngryCat") && password.equals("1234")) {
                result = "loginSuccess";
            } else {
                result = "loginFail";
            }
            return result;
        }



    }
}
