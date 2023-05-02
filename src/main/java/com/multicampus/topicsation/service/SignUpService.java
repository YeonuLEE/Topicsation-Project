package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.repository.ISignUpDAO;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class SignUpService implements ISignUpService {

    @Autowired
    private final ISignUpDAO dao;

    private JavaMailSender mailSender;

    @Override
    public boolean signUpProcess(SignUpDTO signUpDTO) {

        int existEmail = dao.checkEmailDAO(signUpDTO.getEmail());
        int checkEmailAuth = dao.checkEmailAuthDAO(signUpDTO.getEmail());

        if (existEmail != 0) {
            if (checkEmailAuth == 1) {
                System.out.println("이미 존재하는 회원입니다.");
                return false;
            } else {
                System.out.println("이메일 인증을 받지 않은 회원입니다. 기존 정보를 삭제합니다.");
                if(signUpDTO.getRole().equals("tutee")){
                    dao.deleteNotAuthTuteeDAO(signUpDTO.getEmail());
                } else{
                    dao.deleteNotAuthTutorDAO2(signUpDTO.getEmail());
                    dao.deleteNotAuthTutorDAO1(signUpDTO.getEmail());
                }
            }
        }
        System.out.println("신규 회원입니다.");
        //비밀번호 암호화
        signUpDTO.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        // DB에 저장
        if (signUpDTO.getRole().equals("tutee")) {
            dao.addTuteeDAO(signUpDTO);
        } else {
            dao.addTutorDAO1(signUpDTO);
            dao.addTutorDAO2(signUpDTO);

            String userId = dao.getUserId(signUpDTO.getEmail());

            final String UPLOAD_DIR = "src/main/resources/static/assets/certificate/";
            try {
                System.out.println("file : " + signUpDTO.getFile());

                // 파일 저장
                byte[] bytes = signUpDTO.getFile().getBytes();
                String fileExtension = getFileExtension(signUpDTO.getFile().getOriginalFilename());
                String fileName = userId + "." + fileExtension;
                Path path = Paths.get(UPLOAD_DIR + fileName);

                Files.write(path, bytes);

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("email", signUpDTO.getEmail());
                paramMap.put("fileName", fileName);
                dao.saveCertificate(paramMap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    @Override
    public boolean sendMail(MailDTO mailDTO){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailDTO.getEmail());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
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
    public void successEmailAuth(SignUpDTO signUpDTO) {
        dao.successEmailAuthDAO(signUpDTO.getEmail());
    }
}
