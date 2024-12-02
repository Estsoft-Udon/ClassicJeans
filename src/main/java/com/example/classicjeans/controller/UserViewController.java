package com.example.classicjeans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    // 회원 정보
    @GetMapping("/mypage")
    public String myPage() {
        return "/member/mypage";
    }

    // 회원 정보 수정
    @GetMapping("/edit_profile")
    public String editProfile() {
        return "/member/edit_profile";
    }
}
