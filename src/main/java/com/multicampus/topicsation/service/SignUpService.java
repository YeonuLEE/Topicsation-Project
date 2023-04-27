package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.dto.SignUpDTO;
import com.multicampus.topicsation.repository.ISignUpDAO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        if(existEmail != 0) {
            return false;
        }else{
            System.out.println("신규 회원입니다.");
            if(signUpDTO.getRole().equals("tutee")){
                dao.addTuteeDAO(signUpDTO);
            } else {
                dao.addTutorDAO(signUpDTO);
            }
            return true;
        }
    }

    @Override
    public boolean sendMail(MailDTO mailDTO){

        System.out.println("service sendmail" + mailDTO);
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailDTO.getEmail());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());

        System.out.println(message);

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
}
