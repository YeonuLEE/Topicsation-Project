package com.multicampus.topicsation.service;


import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.repository.ILoginDAO;
import com.multicampus.topicsation.repository.ISignUpDAO;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberManageService implements IMemberManageService {

    @Autowired
    private final ILoginDAO loginDao;

    @Autowired
    private final ISignUpDAO checkDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private final BCrypt bCrypt;

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
        if(checkDao.checkEmailAuthDAO(email) == 1) {
            LoginDTO dto = loginDao.login(result);
            if (dto.getRole().equals("tutor")) {
                if (loginDao.checkApproval(dto.getUser_id()) != 1) {
                    return null;
                }
            }
            if(BCrypt.checkpw(password, dto.getPassword())) {
                return dto;
            }
        } return null;
    }

    @Override
    public boolean checkEmail(MailDTO mailDTO) {
        int check = checkDao.checkEmailDAO(mailDTO.getEmail());
        if (check != 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean sendMail(MailDTO mailDTO) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String link = "http://localhost:8081/members/signin/change"; //페이지 링크

        String mailContent = "<html><body>" +
                "<p>비밀번호 재설정을 위해 아래 링크를 눌러주세요.(Please click the link below to reset your password.)</p>" +
                "<br><a href='" + link + "'>" + "topicsation password change link!" + "</a>" +
                "</body></html>";

        helper.setTo(mailDTO.getEmail());
        helper.setFrom("dldusdn8060@gmail.com");
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
    public boolean changePassword(LoginDTO loginDTO) {
        loginDTO.setPassword(BCrypt.hashpw(loginDTO.getPassword(),BCrypt.gensalt()));
        int result = loginDao.changePassword(loginDTO);
        if(result != 1) {
            return false;
        }else{
            return true;
        }
    }
}