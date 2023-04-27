package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MailDTO;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "bornthislee@gmail.com";

    public void sendMail(MailDTO mailDTO){

        System.out.println("service sendmail" + mailDTO);
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailDTO.getToAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());

        try{
            mailSender.send(message);
            System.out.println(mailDTO.getToAddress() + " 메일 전송 성공");
        } catch (MailException e){
            e.printStackTrace();
            System.out.println(mailDTO.getToAddress() + " 메일 전송 실패");
        }
    }
}
