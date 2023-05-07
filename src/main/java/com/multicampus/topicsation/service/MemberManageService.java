package com.multicampus.topicsation.service;


import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.repository.ILoginDAO;
import com.multicampus.topicsation.repository.ISignUpDAO;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class MemberManageService implements IMemberManageService {

    @Autowired
    private final ILoginDAO loginDao;

    @Autowired
    private final ISignUpDAO signUpDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private final IS3FileService s3FileService;
    
    private Map<String, Long> linkExpirationMap = new ConcurrentHashMap<>();

    @Override
    public LoginDTO login(Map<String, String> map) throws Exception {
        String email = map.get("email");
        String password = map.get("password");

        // email과 password가 null일 경우 예외 던짐
        if (email == null || password == null) {
            throw new IllegalArgumentException("이메일과 패스워드를 입력하세요.");
        }
        Map<String, String> result = new HashMap<>();
        result.put("email", email);
        if(signUpDao.checkEmailAuthDAO(email) == 1) {
            LoginDTO dto = loginDao.login(email);
            if(dto != null && BCrypt.checkpw(password, dto.getPassword())) {
                if (dto.getRole().equals("tutor") && loginDao.checkApproval(dto.getUser_id()) != 1) {
                    return null;
                }
                return dto;
            }
        } return null;
    }

    @Override
    public boolean checkEmail(MailDTO mailDTO) {
        int emailCheck = signUpDao.checkEmailDAO(mailDTO.getEmail());
        return emailCheck == 1;
    }

    @Override
    public boolean sendMail(MailDTO mailDTO) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String linkId = UUID.randomUUID().toString();
        String link = "http://localhost:8081/members/signin/change?linkId=" + linkId; //페이지 링크

        long expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5); // 만료 시간 설정
        linkExpirationMap.put(linkId, expirationTime); // 링크별 만료 시간 저장

        String mailContent = "<html><body>" +
                "<p>비밀번호 재설정을 위해 아래 링크를 눌러주세요.(Please click the link below to reset your password.)</p>" +
                "<br><a href='" + link + "'>" + "topicsation password change link!" + "</a>" +
                "</body></html>";

        helper.setTo(mailDTO.getEmail());
        helper.setText(mailContent, true);
        helper.setSubject("TOPICSATION 비밀번호 재설정 링크입니다.");

        try {
            mailSender.send(message);
            return true;
        }catch(MailException e) {
            return false;
        }
    }

    @Override
    public Long getLinkExpirationMap(String linkId) {
        return linkExpirationMap.get(linkId);
    }

    @Override
    public boolean changePassword(LoginDTO loginDTO) {
        loginDTO.setPassword(BCrypt.hashpw(loginDTO.getPassword(),BCrypt.gensalt()));
        int result = loginDao.changePassword(loginDTO);
        return result == 1;
    }

    @Override
    public boolean signUpProcess(SignUpDTO signUpDTO) {

        int existEmail = signUpDao.checkEmailDAO(signUpDTO.getEmail());
        int checkEmailAuth = signUpDao.checkEmailAuthDAO(signUpDTO.getEmail());

        if (existEmail != 0) {
            if (checkEmailAuth == 1) {
                System.out.println("이미 존재하는 회원입니다.");
                return false;
            } else {
                System.out.println("이메일 인증을 받지 않은 회원입니다. 기존 정보를 삭제합니다.");
                if(signUpDTO.getRole().equals("tutee")){
                    signUpDao.deleteNotAuthTuteeDAO(signUpDTO.getEmail());
                } else{
                    signUpDao.deleteNotAuthTutorDAO(signUpDTO.getEmail());
                }
            }
        }
        System.out.println("신규 회원입니다.");
        //비밀번호 암호화
        signUpDTO.setPassword(BCrypt.hashpw(signUpDTO.getPassword(), BCrypt.gensalt()));
        // DB에 저장
        if (signUpDTO.getRole().equals("tutee")) {
            signUpDao.addTuteeDAO(signUpDTO);
        } else {
            signUpDao.addTutorDAO1(signUpDTO);
            String userId = signUpDao.getUserId(signUpDTO.getEmail());
            System.out.println("file : " + signUpDTO.getFile());
            String bucketName = "asset";
            String folderName = "certificate";
            // 확장자 추출
            String fileExtension = getFileExtension(signUpDTO.getFile().getOriginalFilename());
            String fileName = userId + "." + fileExtension;

            String objectKey = folderName + "/" + fileName; // "profile/example.jpg"와 같은 형식으로 생성됩니다.
            s3FileService.uploadFile(bucketName, objectKey, signUpDTO.getFile());
            signUpDTO.setCertificate(fileName);
            signUpDao.addTutorDAO2(signUpDTO);
        }
        return true;
    }

    @Override
    public String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없는 경우
        }
        // 확장자 반환
        return fileName.substring(lastIndexOfDot + 1);
    }

    @Override
    public boolean signupSendMail(MailDTO mailDTO){

        // 랜덤 인증코드
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111); // 범위: 111111~999999
        mailDTO.setAuthKey(authKey);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailDTO.getEmail());
        message.setSubject("TOPICSATION 인증코드입니다");
        message.setText("인증코드: " + mailDTO.getAuthKey());

        System.out.println("[message] "+ message);

        try{
            mailSender.send(message);
            System.out.println(mailDTO.getEmail() + " 메일 전송 성공");
            return true;
        } catch (MailException e){
            e.printStackTrace();
            System.out.println(mailDTO.getEmail() + " 메일 전송 실패");
            return false;
        }
    }

    @Override
    public boolean isSuccessEmailAuth(SignUpDTO signUpDTO) {
        try{
            signUpDao.isSuccessEmailAuthDAO(signUpDTO.getEmail());
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}