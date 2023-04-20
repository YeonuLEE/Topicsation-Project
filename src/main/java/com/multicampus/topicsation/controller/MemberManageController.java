package com.multicampus.topicsation.controller;

import org.json.simple.JSONObject;
import org.springframework.boot.Banner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/members")
public class MemberManageController {

    @GetMapping("/admin")
    public String admin(){
        return "html/sign-in-admin";
    }

    @GetMapping("/signin")
    public String signin(){
        return "html/sign-in";
    }

    @GetMapping("/signin/find")
    public String passwordFind(){
        return "html/password-find";
    }

    @GetMapping("/signin/change")
    public String passwordChange(){
        return "html/password-change";
    }

    @GetMapping("/signup-tutees")
    public String signupTutees(){
        return "html/sign-up-tutee";
    }

    @GetMapping("/signup-tutors")
    public String signupTutors(){
        return "html/sign-up-tutor";
    }

    @GetMapping("/signup/email")
    public String emailAuth(){
        return "html/Email-Token";
    }

    @GetMapping("/signup/success")
    public String success(){
        return "html/sign-up-success";
    }

    @RestController
    @RequestMapping("/members")
    public class MemberManageRestController{

        @PostMapping("/signup-tutees.post")
        public String signUpTutee(@RequestBody JSONObject jsonObject){
            String result;
            String email = jsonObject.get("$email").toString();
            String password = jsonObject.get("$password").toString();
            String name = jsonObject.get("$name").toString();
            String firstInterest = jsonObject.get("$firstInterest").toString();
            String secondInterest = jsonObject.get("$secondInterest").toString();

            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()
                    && !firstInterest.isEmpty() && !secondInterest.isEmpty()){
                result="signupSuccess";
            }else{
                result="signupFail";
            }
            return result;
        }

//        @RequestMapping(value = "/signup-tutors.post", method = RequestMethod.POST)
        @PostMapping("/signup-tutors.post")
        public String signUpTutor(@RequestBody JSONObject jsonObject){
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
                    && !firstInterest.isEmpty() && !secondInterest.isEmpty() && !gender.isEmpty()){
                result="success";
            }else{
                result="fail";
            }
            System.out.println("result : " + result);
            return result;
        }
    }
}
