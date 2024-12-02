package com.example.classicjeans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    // 마이페이지
    @GetMapping("/mypage")
    public String myPage() {
        return "/member/mypage";
    }
}
