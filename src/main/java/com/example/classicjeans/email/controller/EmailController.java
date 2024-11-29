package com.example.classicjeans.email.controller;

import com.example.classicjeans.email.AuthCodeGenerator;
import com.example.classicjeans.email.service.AuthService;
import com.example.classicjeans.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final AuthService authService;

    @ResponseBody
    @PostMapping("/send-email")
    public String sendEmail(@RequestParam(required = false) String email) {
        String authCode = AuthCodeGenerator.generateCode(6); // 6자리 랜덤 숫자 생성
        authService.saveAuthCode(email, authCode);
        emailService.sendEmail(email, authCode);
        return "이메일 전송 완료!";
    }
}
