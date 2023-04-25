package com.multicampus.topicsation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.service.IMemberManageService;
import com.multicampus.topicsation.service.security.CustomUserDetailsService;
import com.multicampus.topicsation.token.JwtFilter;
import com.multicampus.topicsation.token.TokenProvider;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.SecureRandom;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/members")
public class MemberManageController {

    @GetMapping("/admin")
    public String admin() {
        return "html/sign-in-admin";
    }

    @GetMapping("/signin")
    public String signin() {
        System.out.println("login!");
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
    public String signupTutees() {
        return "html/sign-up-tutee";
    }

    @GetMapping("/signup-tutors")
    public String signupTutors() {
        return "html/sign-up-tutor";
    }

    @GetMapping("/signup/email")
    public String emailAuth() {
        return "html/Email-Token";
    }

    @GetMapping("/signup/success")
    public String success() {
        return "html/sign-up-success";
    }

    @RestController
    @RequestMapping("/members")
    public class MemberManageRestController {

        //토큰 주입
        private final TokenProvider tokenProvider;
        private final AuthenticationManagerBuilder authenticationManagerBuilder;

        public MemberManageRestController(TokenProvider tokenProvider,
                                          AuthenticationManagerBuilder authenticationManagerBuilder) {
            this.tokenProvider = tokenProvider;
            this.authenticationManagerBuilder = authenticationManagerBuilder;
        }

        @PostMapping("/signup-tutees.post")
        public String signUpTutee(@RequestBody JSONObject jsonObject) {
            String result;
            String email = jsonObject.get("$email").toString();
            String password = jsonObject.get("$password").toString();
            String name = jsonObject.get("$name").toString();
            String firstInterest = jsonObject.get("$firstInterest").toString();
            String secondInterest = jsonObject.get("$secondInterest").toString();


            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()
                    && !firstInterest.isEmpty() && !secondInterest.isEmpty()) {
                result = "signupSuccess";
            } else {
                result = "signupFail";
            }
            return result;
        }

        @PostMapping("/signin.post")
        public String signin(@RequestBody Map<String, String> params) throws JsonProcessingException {

            String email = params.get("email");
            System.out.println(email);
            String password = params.get("password");
            System.out.println(password);

            //더미 데이터 암호화 테스트 진행
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println(hashedPassword);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

            System.out.println("authenticationToken: "+authenticationToken);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            System.out.println("authenticationManagerBuilder: "+authenticationManagerBuilder.getObject().toString());
            System.out.println("authenticatication"+authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("authenticatication"+ SecurityContextHolder.getContext());

            String token = tokenProvider.createToken(authentication);
            System.out.println(token);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer " + token);
            System.out.println(httpHeaders);

            System.out.println(token);

            return token;
        }

        @PostMapping("/signout")
        public void signout() {
            System.out.println("signout...");
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

        @PostMapping("/email.auth")
        public String emailAuth(@RequestBody JSONObject jsonObject) {
            System.out.println(jsonObject.get("test"));
            return "success";
        }

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
    }
}
