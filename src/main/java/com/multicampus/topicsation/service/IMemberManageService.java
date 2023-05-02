package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MailDTO;

import javax.mail.MessagingException;
import java.util.Map;

public interface IMemberManageService {
   public LoginDTO login(Map<String, String> map) throws Exception;
   //이메일 존재 여부 체크
   public boolean checkEmail(MailDTO mailDTO);
   //이메일 전송
   public boolean sendMail(MailDTO mailDTO) throws MessagingException;
   //비밀번호 수정
   public boolean changePassword(LoginDTO loginDTO);
}
