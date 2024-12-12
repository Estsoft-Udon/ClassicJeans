package com.example.classicjeans.controller.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class TestController {

    @Value("${KAKAO_CLIENT_ID")
    private String KAKAO_CLIENT_ID;

    @GetMapping("/check-env")
    public String checkEnv() {
        return KAKAO_CLIENT_ID;
    }
}
