package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.LoginDTO;
import com.multicampus.topicsation.dto.MailDTO;
import com.multicampus.topicsation.dto.SignUpDTO;

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
   // 회원가입
   boolean signUpProcess(SignUpDTO signUpDTO);
   // 회원가입 인증코드 이메일 전송
   boolean signupSendMail(MailDTO mailDTO);
   // 이메일인증을 성공했는지 확인
   boolean isSuccessEmailAuth(Map<String,String> mapParam);
   // 튜터 인증 파일 업로드
   String getFileExtension(String fileName);
   // 비밀번호 재설정 링크 만료 시간 저장
   Long getLinkExpirationMap(String linkId);
}
