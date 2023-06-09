package com.multicampus.topicsation.controller;

import com.multicampus.topicsation.dto.SignUpDTO;
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

@Controller
@RequestMapping("/members")
public class MemberManageController {

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
    public String passwordChange(@RequestParam String linkId) {
        Long expirationTime = memberManageservice.getLinkExpirationMap(linkId); // 링크별 만료 시간 조회
        if (expirationTime == null || System.currentTimeMillis() > expirationTime) {
            // 만료된 링크 처리
            return "error/404";
        }
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
        private final JwtUtils jwtUtils;

        public MemberManageRestController(JwtUtils jwtUtils) {
            this.jwtUtils = jwtUtils;
        }

        @PostMapping("/signin.post")
        public ResponseEntity<String> signin(@RequestBody Map<String, String> params, HttpServletResponse response) throws Exception {
              //email과 password 검증
            LoginDTO dto = memberManageservice.login(params);

            if (dto != null && dto.getApproval().equals("1")) {
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
                return ResponseEntity.ok(dto.getApproval());
            }else if(dto != null && dto.getApproval().equals("0")) {
                return ResponseEntity.ok(dto.getApproval());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        @PostMapping("/signin/change.post")
        public ResponseEntity<Object> passwordChange(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
            HttpSession session = request.getSession();

            // Session에 담긴 email 정보 가져오기
            String email = (String) session.getAttribute("email");
            loginDTO.setEmail(email);
            session.removeAttribute("email");

            // 비밀번호 변경
            if(memberManageservice.changePassword(loginDTO)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @PostMapping("/signin/find/post")
        public ResponseEntity<Object> passwordFind(@RequestBody MailDTO mailDTO, HttpServletRequest request) {
            //유효한 이메일인지 확인
            if(memberManageservice.checkEmail(mailDTO)) {
                //세션 과정은 컨트롤러에서 진행하는게 좋음!
                HttpSession session = request.getSession();
                //입력 받은 email 정보 세션에 저장
                session.setAttribute("email", mailDTO.getEmail());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        @PostMapping("/signin/find/email.send")
        public ResponseEntity<Object> passwordChange(@RequestBody MailDTO mailDTO) throws MessagingException {
            boolean result = memberManageservice.sendMail(mailDTO);
            if(result) {
                return ResponseEntity.ok().build();
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @PostMapping("/signup-tutees.post")
        public ResponseEntity<String> signUpTutee(@RequestBody SignUpDTO signUpDTO) {
            if (memberManageservice.signUpProcess(signUpDTO)) {
                return ResponseEntity.ok(signUpDTO.getEmail());
            } else {
                return ResponseEntity.internalServerError().build();
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

            SignUpDTO signUpDTO = SignUpDTO.builder()
                    .email(email)
                    .name(name)
                    .password(password)
                    .gender(gender)
                    .nationality(nationality)
                    .firstInterest(firstInterest)
                    .secondInterest(secondInterest)
                    .role(role)
                    .file(file)
                    .build();

            if (memberManageservice.signUpProcess(signUpDTO)) {
                return ResponseEntity.ok(signUpDTO.getEmail());
            } else {
                return ResponseEntity.internalServerError().build();
            }
        }

        @PostMapping("/email.send")
        public ResponseEntity<Void> emailAuth(@RequestBody MailDTO mailDTO) {
            if(memberManageservice.signupSendMail(mailDTO)){
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.internalServerError().build();
            }
        }

        @PostMapping("/signup/success.post")
        public ResponseEntity<Void> successEmailAuth(@RequestBody Map<String,String> mapParam) {
            if(memberManageservice.isSuccessEmailAuth(mapParam)){
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }
}
