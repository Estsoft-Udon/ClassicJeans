package com.example.classicjeans.controller;

import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UsersService usersService;

    // 회원 정보
    @GetMapping("/mypage")
    public String myPage(Model model) {
        // 추후에 로그인 중인 유저 정보로 변경 필요
        Users user = usersService.findUserById(8L);
        model.addAttribute("user", user);
        return "/member/mypage";
    }

    // 회원 정보 수정
    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        // 추후에 로그인 중인 유저 정보로 변경 필요
        Users user = usersService.findUserById(8L);
        model.addAttribute("user", user);
        return "/member/edit_profile";
    }

    // 가족정보수정
    @GetMapping("/edit_family")
    public String editFamily() {
        return "/member/edit_family";
    }

    // 비밀번호 번경
    @GetMapping("/change_pw")
    public String changePassword() {
        return "/member/change_pw";
    }

    // 회원 탈퇴
    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "/member/withdrawal";
    }
}
