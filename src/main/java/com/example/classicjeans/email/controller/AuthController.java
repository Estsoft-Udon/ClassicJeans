package com.example.classicjeans.email.controller;

import com.example.classicjeans.email.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyAuthCode(@RequestParam String email, @RequestParam String authCode) {
        if (authService.validateAuthCode(email, authCode)) {
            authService.updateAuthStatus(email); // 인증 상태 업데이트
            return ResponseEntity.ok("인증 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패: 인증번호가 올바르지 않거나 만료되었습니다.");
        }
    }

    @PostMapping("/updateAuthStatus")
    public ResponseEntity<String> updateAuthStatus(@RequestParam String email) {
        // 인증 상태를 세션에 저장
        authService.updateAuthStatus(email);

        return ResponseEntity.ok("인증 상태가 세션에 저장되었습니다.");
    }
}