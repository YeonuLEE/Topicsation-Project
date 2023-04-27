package com.multicampus.topicsation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.dto.MemberRole;
import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.service.MailService;
import com.multicampus.topicsation.service.SignUpService;
import com.multicampus.topicsation.token.JwtFilter;
import com.multicampus.topicsation.token.TokenProvider;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        private final AuthenticationManagerBuilder authenticationManagerBuilder;

        public MemberManageRestController(TokenProvider tokenProvider,
                                          AuthenticationManagerBuilder authenticationManagerBuilder) {
            this.tokenProvider = tokenProvider;
            this.authenticationManagerBuilder = authenticationManagerBuilder;
        }

        @PostMapping("/signup-tutees.post")
        public String signUpTutee(@RequestBody JSONObject jsonObject) {

            String email = jsonObject.get("$email").toString();
            String password = jsonObject.get("$password").toString();
            String name = jsonObject.get("$name").toString();
            String firstInterest = jsonObject.get("$firstInterest").toString();
            String secondInterest = jsonObject.get("$secondInterest").toString();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                    || firstInterest.isEmpty() || secondInterest.isEmpty()) {
                System.out.println("signup Fail");
                return "signupFail";
            }

            SignUpDTO dto = new SignUpDTO();
            dto.setEmail(jsonObject.get("$email").toString());
            dto.setPassword(jsonObject.get("$password").toString());
            dto.setName(jsonObject.get("$name").toString());
            dto.setInterest1(jsonObject.get("$firstInterest").toString());
            dto.setInterest2(jsonObject.get("$secondInterest").toString());
            dto.setRole(MemberRole.TUTEE);

            signUpService.checkEmail(dto);
            signUpService.addTutee(dto);
            System.out.println(dto);

            return "signupSuccess";
        }
//        @PostMapping("/signup-tutees.post")
//        public String signUpTutee(@RequestBody JSONObject jsonObject) {
//            String result;
//            String email = jsonObject.get("$email").toString();
//            String password = jsonObject.get("$password").toString();
//            String name = jsonObject.get("$name").toString();
//            String firstInterest = jsonObject.get("$firstInterest").toString();
//            String secondInterest = jsonObject.get("$secondInterest").toString();
//
//
//            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()
//                    && !firstInterest.isEmpty() && !secondInterest.isEmpty()) {
//                result = "signupSuccess";
//            } else {
//                result = "signupFail";
//            }
//            return result;
//        }

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
        public String emailAuth(@RequestBody JSONObject jsonObject) throws Exception {


            // 결과반환
            return "success!";
        }

//        @PostMapping("/email.auth")
//        public void execMail(MailDTO mailDTO) {
//            mailService.mailSend(mailDTO);
//        }

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

//        @PostMapping("/signup-tutors.post")
//        public String signUpTutor(@RequestBody JSONObject jsonObject) {
//            String result;
//            String email = jsonObject.get("$email").toString();
//            String password = jsonObject.get("$password").toString();
//            String name = jsonObject.get("$name").toString();
//            String gender = jsonObject.get("$gender").toString();
//            String nationality = jsonObject.get("$nationality").toString();
//            String firstInterest = jsonObject.get("$firstInterest").toString();
//            String secondInterest = jsonObject.get("$secondInterest").toString();
//            //String formData = jsonObject.get("$formData").toString();
//
//            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !nationality.isEmpty()
//                    && !firstInterest.isEmpty() && !secondInterest.isEmpty() && !gender.isEmpty()) {
//                result = "success";
//            } else {
//                result = "fail";
//            }
//            System.out.println("result : " + result);
//            return result;
//        }
    }
}
