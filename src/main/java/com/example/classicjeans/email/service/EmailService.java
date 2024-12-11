package com.example.classicjeans.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String authCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("ephigh1397@gmail.com"); // 발신자 이메일
            helper.setTo(to);                      // 수신자 이메일
            helper.setSubject("청바지 이메일 인증 코드");            // 이메일 제목
            helper.setText("인증 코드: " + authCode, false);            // 이메일 본문 (HTML 사용 시 true)

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패: " + e.getMessage());
        }
    }
}