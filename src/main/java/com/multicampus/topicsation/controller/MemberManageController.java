package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.service.ISignUpService;
import com.multicampus.topicsation.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.service.IMemberManageService;
import com.multicampus.topicsation.service.SignUpService;
//import com.multicampus.topicsation.service.security.CustomUserDetailsService;
import com.multicampus.topicsation.token.JwtUtils;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/members")
public class MemberManageController {

    @Autowired
    ISignUpService signUpService;

    @Autowired
    private IMemberManageService service;

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
    public String emailAuth(String email, HttpSession session) throws Exception {


        return "html/Email-Token";
    }

    @GetMapping("/signup/success")
    public String success(HttpSession session) {
        return "html/sign-up-success";
    }

    @RestController
    @RequestMapping("/members")
    public class MemberManageRestController {

        //토큰 주입
        private final JwtUtils jwtUtils;

        public MemberManageRestController(JwtUtils jwtUtils) {
            this.jwtUtils = jwtUtils;
        }

        @PostMapping("/signup-tutees.post")
        public String signUpTutee(@RequestBody SignUpDTO signUpDTO) {
            boolean result = signUpService.signUpProcess(signUpDTO);
            if (result) {
                return signUpDTO.getEmail();
            } else {
                return "signupFail";
            }
        }

        @PostMapping("/signin.post")
        public ResponseEntity<Object> signin(@RequestBody Map<String, String> params, HttpServletResponse response) throws Exception {

            String email = params.get("email");
            System.out.println(email);
            String password = params.get("password");

              //email과 password 검증
            LoginDTO dto = service.login(params);

            if (dto != null && BCrypt.checkpw(password, dto.getPassword())) {
                //accesstoken 생성
                String accessToken = jwtUtils.createAccessToken(dto.getRole(), dto.getUser_id());

                //refreshtoken 생성
                String refreshToken = jwtUtils.createRefreshToken(dto.getRole(), dto.getUser_id());

                //Header에 accesstoken 정보 담아서 응답
                response.setHeader("Authorization", "Bearer " + accessToken);

                //쿠키설정
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                //cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(86400);

                //Cookie에 refreshtoken 정보 담아서 응답
                response.addCookie(cookie);

                //200 전달
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

        @PostMapping("/email.send")
        public String emailAuth(@RequestBody MailDTO mailDTO) {
            boolean result;
            Random random = new Random();
            String authKey = String.valueOf(random.nextInt(888888) + 111111); // 범위: 111111~999999

            mailDTO.setTitle("TOPICSATION 인증코드입니다.");
            mailDTO.setMessage("인증코드: " + authKey);

            result = signUpService.sendMail(mailDTO);

            if(result){
                return authKey;
            }
            else {
                return "sendFail";
            }
        }

        @PostMapping("/signup-tutors.post")
        public String signUpTutor(@RequestBody SignUpDTO signUpDTO) {
            boolean result = signUpService.signUpProcess(signUpDTO);
            if (result) {
                return signUpDTO.getEmail();
            } else {
                return "signupFail";
            }
        }

        @PostMapping("/signup/success.post")
        public String successEmailAuth(@RequestBody SignUpDTO signUpDTO) {
            System.out.println("controller email확인" + signUpDTO.getEmail());
            signUpService.successEmailAuth(signUpDTO);
//        // 세션 초기화
//        session.invalidate();
            return "emailAuthSuccess";
        }
    }
}
