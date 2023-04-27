package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.MemberRole;
import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.service.IMemberManageService;
import com.multicampus.topicsation.service.SignUpService;
import com.multicampus.topicsation.service.security.CustomUserDetailsService;
import com.multicampus.topicsation.token.JwtFilter;
import com.multicampus.topicsation.token.TokenProvider;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.SecureRandom;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/members")
public class MemberManageController {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private MailService mailService;

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
    public String emailAuth(String email) throws Exception{

        email = "ah_611@naver.com";
        System.out.println("controller get email:" + email);
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111); // 범위: 111111~999999

        MailDTO mailDTO = new MailDTO();
        mailDTO.setToAddress(email);
        mailDTO.setTitle("TOPICSATION 인증코드입니다.");
        mailDTO.setMessage("인증코드: " + authKey);

        mailService.sendMail(mailDTO);
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
        private final BCrypt bCrypt;

        @Autowired
        private IMemberManageService service;
        //        private final AuthenticationManagerBuilder authenticationManagerBuilder;
//
        public MemberManageRestController(TokenProvider tokenProvider, BCrypt bCrypt) {
            this.tokenProvider = tokenProvider;
            this.bCrypt = bCrypt;
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
//            //더미 데이터 암호화 테스트 진행
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////            String hashedPassword = passwordEncoder.encode(password);
////            System.out.println(hashedPassword);
//
            String email = params.get("email");
            System.out.println(email);
            String password = params.get("password");
            System.out.println(password);
//
//          //email과 password 검증
            LoginDTO dto = service.login(params);

            if (dto == null || !BCrypt.checkpw(password, dto.getPassword())) {
//                    throw new IllegalArgumentException("일치하는 회원이 없습니다.");
                return "일치하는 회원이 없습니다.";
            } else {
                //accesstoken 생성
                String token = tokenProvider.createAccessToken(dto.getEmail(), dto.getRole());
                System.out.println(token);

                //accesstioken 반환
                return token;
            }
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
        public boolean signUpTutor(@RequestBody Map<String, String> jsonMap) {

            String email = jsonMap.get("$email").toString();
            String password = jsonMap.get("$password").toString();
            String name = jsonMap.get("$name").toString();
            String gender = jsonMap.get("$gender").toString();
            String nationality = jsonMap.get("$nationality").toString();
            String firstInterest = jsonMap.get("$firstInterest").toString();
            String secondInterest = jsonMap.get("$secondInterest").toString();
//            String certificate = jsonMap.get("$certificate").toString();

            System.out.println(password);


            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || gender.isEmpty() || nationality.isEmpty()
                    || firstInterest.isEmpty() || secondInterest.isEmpty()) {
                System.out.println("signup Fail");
                return false;
            }

            SignUpDTO dto = new SignUpDTO();
            dto.setEmail(jsonMap.get("$email").toString());
            dto.setPassword(jsonMap.get("$password").toString());
            dto.setName(jsonMap.get("$name").toString());
            dto.setGender(jsonMap.get("$gender").toString());
            dto.setNationality(jsonMap.get("$nationality").toString());
            dto.setInterest1(jsonMap.get("$firstInterest").toString());
            dto.setInterest2(jsonMap.get("$secondInterest").toString());
//            dto.setCertificate(jsonMap.get("$certificate").toString());
            dto.setRole(MemberRole.TUTOR);

            System.out.println("controller: " + dto);
            signUpService.addTutor(dto);

            return true;
        }
    }
}
