package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.service.ISignUpService;
import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.service.IMemberManageService;
import com.multicampus.topicsation.token.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
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
    private IMemberManageService memberManageservice;

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
    public String signupTutees() {
        return "html/sign-up-tutee";
    }

    @GetMapping("/signup-tutors")
    public String signupTutors() {
        return "html/sign-up-tutor";
    }

    @GetMapping("/signup/email")
    public String emailAuth() throws Exception {
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

              //email과 password 검증
            LoginDTO dto = memberManageservice.login(params);

            if (dto != null) {
                //accesstoken 생성
                String accessToken = jwtUtils.createAccessToken(dto.getRole(), dto.getUser_id());

                //refreshtoken 생성
                String refreshToken = jwtUtils.createRefreshToken(dto.getRole(), dto.getUser_id());

                //Header에 accesstoken 정보 담아서 응답
                response.setHeader("Authorization", "Bearer " + accessToken);

                //쿠키설정
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setPath("/");

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
        public ResponseEntity<Object> passwordChange(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
            System.out.println("passwordChange method");
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("email");
            session.removeAttribute("email");
            loginDTO.setEmail(email);
            if(memberManageservice.changePassword(loginDTO)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @PostMapping("/singin/find/post")
        public ResponseEntity<Object> passwordFind(@RequestBody MailDTO mailDTO, HttpServletRequest request) {
            if(memberManageservice.checkEmail(mailDTO)) {
                //세션 과정은 컨트롤러에서 진행하는게 좋음!
                HttpSession session = request.getSession();
                session.setAttribute("email", mailDTO.getEmail());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @PostMapping("/signin/find/email.send")
        public ResponseEntity<Object> passwordChange(@RequestBody MailDTO mailDTO) throws MessagingException {
            boolean result = memberManageservice.sendMail(mailDTO);
            if(result) {
                return ResponseEntity.unprocessableEntity().build();
            }else {
                return ResponseEntity.unprocessableEntity().build();
            }
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
        public ResponseEntity<String> signUpTutor(
                @RequestParam("email") String email,
                @RequestParam("name") String name,
                @RequestParam("password") String password,
                @RequestParam("genderRadios") String gender,
                @RequestParam("nationality") String nationality,
                @RequestParam("firstInterest") String firstInterest,
                @RequestParam("secondInterest") String secondInterest,
                @RequestParam("role") String role,
                @RequestParam("customFile") MultipartFile file) {

            SignUpDTO signUpDTO = new SignUpDTO();

            signUpDTO.setEmail(email);
            signUpDTO.setName(name);
            signUpDTO.setPassword(password);
            signUpDTO.setGender(gender);
            signUpDTO.setNationality(nationality);
            signUpDTO.setFirstInterest(firstInterest);
            signUpDTO.setSecondInterest(secondInterest);
            signUpDTO.setRole(role);
            signUpDTO.setFile(file);

            System.out.println(signUpDTO);

            boolean result = signUpService.signUpProcess(signUpDTO);
            if (result) {
                return new ResponseEntity <String> (signUpDTO.getEmail(), HttpStatus.OK);
            } else {
                return new ResponseEntity <String> ("signupFail", HttpStatus.OK);
            }
        }

        @PostMapping("/signup/success.post")
        public String successEmailAuth(@RequestBody SignUpDTO signUpDTO) {
            boolean result = signUpService.successEmailAuth(signUpDTO);
            if(result){
                return "emailAuthSuccess";
            } else {
                return "emailAuthFail";
            }
        }
    }
}
