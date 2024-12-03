package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/edit-profile")
    public String editProfile(@ModelAttribute UsersRequest request, Model model) {
        // 추후에 로그인 중인 유저 정보로 변경 필요
        Users user = usersService.findUserById(8L);
        usersService.update(user.getId(), request);
        return "redirect:/mypage";
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

    // 비밀번호 변경 (찾기 후)
    @GetMapping("/change_pw_after_find")
    public String changePasswordAfterFind() {
        return "member/change_pw_after_find";
    }

    // 회원 탈퇴
    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "/member/withdrawal";
    }

    // 회원가입
    @GetMapping("/signup")
    public String signup(Model model) {

        return "member/signup";
    }

    @GetMapping("/success")
    public String success() {
        return "member/success";
    }

    @GetMapping("/find_id")
    public String findId() {
        return "member/find_id";
    }

    @GetMapping("/find_pw")
    public String findPw(Model model) {

        return "member/find_pw";
    }

    // 접근 제한
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
