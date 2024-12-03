package com.example.classicjeans.controller;


import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.email.service.AuthService;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UsersService usersService;
    private final AuthService authService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        HttpSession session,
                        Model model) {

        if (error != null) {
            String errorMessage = (String) session.getAttribute("error");
            model.addAttribute("error", errorMessage);

            session.removeAttribute("error");
        }

        return "member/login";
    }

    @GetMapping("/find_id")
    public String findId() {
        return "member/find_id";
    }

    @PostMapping("/find_id")
    public String findId(String name, String email, Model model) {
        Users foundUser = usersService.searchId(name, email);

        if (foundUser != null) { // 아이디가 발견된 경우
            String loginId = foundUser.getLoginId();
            model.addAttribute("foundId", loginId);
            model.addAttribute("isIdFound", true);

        } else {  // 아이디 발견 여부 플래그
            model.addAttribute("isIdFound", false);
        }
        return "member/find_id";
    }

    @GetMapping("/find_pw")
    public String findPw(Model model) {

        return "member/find_pw";
    }

    @PostMapping("/find_pw")
    public String findPw(Model model, String loginId) {

        return "redirect:/find_pw";
    }

    @GetMapping("/change_pw")
    public String changePw(Model model, String loginId) {
//        if (SecurityUtil.getLoggedInUser() != null) {
//            Users user = usersService.findUserById(SecurityUtil.getLoggedInUser().getId());
//            model.addAttribute("user", user);
//        } else {
//            model.addAttribute("loginId", loginId);
//        }

        return "member/change_pw";
    }

    // 비밀번호 변경 처리 (POST)
    @PostMapping("/change_pw")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Model model) {
        boolean isUpdated = false;
//        if (getLoggedInUser() != null) {
//            isUpdated = usersService.changePassword(getLoggedInUser().getId(), currentPassword, newPassword);
//        }

        if (isUpdated) {
            model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            return "member/change_pw";
        } else {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/change_pw";
        }
    }

    @PostMapping("/change_pw_after_find")
    public String changePasswordAfterFind(@RequestParam String newPassword,
                                          @ModelAttribute("loginId") String loginId) {

        usersService.changePasswordAfterFind(loginId, newPassword);

        return "member/change_pw";
    }



    // 회원가입
    @GetMapping("/signup")
    public String signup(Model model) {

        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UsersRequest request,
                         Model model) {
        try {
            if (!authService.isEmailVerified(request.getEmail())) {
                model.addAttribute("error", "이메일 인증이 완료되지 않았습니다.");
                return "member/signup";
            }

            usersService.register(request);
            return "redirect:/success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "member/signup";
        }
    }

    @GetMapping("/success")
    public String success() {
        return "member/success";
    }

    // 회원 탈퇴
    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "member/withdrawal";
    }

//    @GetMapping("/mypage")
//    public String mypage(Model model) {
//        Users userById = usersService.findUserById(getLoggedInUser().getId());
//        model.addAttribute("user", userById);
//        return "member/mypage";
//    }

//    @GetMapping("/edit_profile")
//    public String editProfile(Model model) {
//        // 로그인 사용자의 정보
//        Users users = usersService.findUserById(getLoggedInUser().getId());
//        model.addAttribute("user", users);
//
//        return "member/edit_profile";
//    }
//
//    @PostMapping("/edit_profile")
//    public String editProfile(@ModelAttribute UsersRequest request) {
//        usersService.updateUser(getLoggedInUser().getId(), request);
//        return "redirect:/mypage";
//    }

    // 접근 제한
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}