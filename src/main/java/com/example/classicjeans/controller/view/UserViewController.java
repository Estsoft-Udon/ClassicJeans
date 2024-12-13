package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.email.service.AuthService;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.FamilyInfoService;
import com.example.classicjeans.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

import static com.example.classicjeans.util.SecurityUtil.*;

@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UsersService usersService;
    private final AuthService authService;
    private final FamilyInfoService familyInfoService;

    // 로그인
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

    // 회원가입
    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UsersRequest request, HttpSession session, Model model) {
        try {
            if (!authService.isEmailVerified(request.getEmail())) {
                model.addAttribute("error", "이메일 인증이 완료되지 않았습니다.");
                return "member/signup";
            }

            String uniqueKey = (String) session.getAttribute("uniqueKey");
            if (uniqueKey != null) {
                request.setUniqueKey(uniqueKey);
            }

            usersService.register(request);

            session.removeAttribute("uniqueKey");

            return "redirect:/success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "member/signup";
        }
    }

    // 회원가입 성공시
    @GetMapping("/success")
    public String success() {
        return "member/success";
    }

    // 아이디 찾기
    @GetMapping("/find-id")
    public String findId() {
        return "member/find-id";
    }

    @PostMapping("/find-id")
    public String findId(String name, String email, Model model) {
        Users foundUser = usersService.searchId(name, email);

        if (foundUser != null) { // 아이디가 발견된 경우
            String loginId = foundUser.getLoginId();
            model.addAttribute("foundId", loginId);
            model.addAttribute("isIdFound", true);

        } else {  // 아이디 발견 여부 플래그
            model.addAttribute("isIdFound", false);
        }
        return "member/find-id";
    }

    // 비밀번호 찾기
    @GetMapping("/find-pw")
    public String findPw() {
        return "member/find-pw";
    }

    // 비밀번호 번경
    @GetMapping("/change-pw")
    public String changePassword() {
        return "member/change-pw";
    }

    // 비밀번호 변경 처리 (POST)
    @PostMapping("/change-pw")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Model model) {
        boolean isUpdated = false;
        if (getLoggedInUser() != null) {
            isUpdated = usersService.changePassword(getLoggedInUser().getId(), currentPassword, newPassword);
        }

        if (isUpdated) {
            model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            return "member/change-pw";
        } else {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/change-pw";
        }
    }

    // 비밀번호 변경 (찾기 후)
    @GetMapping("/change-pw-after-find")
    public String changePasswordAfterFind() {
        return "member/change-pw-after-find";
    }

    // 비밀번호 찾기 후속 처리
    @PostMapping("/change-pw-after-find")
    public String changePasswordAfterFind(String newPassword, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");

        if (loginId == null) {
            model.addAttribute("errorMessage", "비밀번호 변경에 실패하였습니다.");
            throw new IllegalStateException("비밀번호 변경 요청이 유효하지 않습니다.");
        }

        model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        usersService.changePasswordAfterFind(loginId, newPassword);

        return "member/change-pw";
    }

    // 회원 정보
    @GetMapping("/mypage")
    public String myPage(Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);

        List<FamilyInfoResponse> familyInfoList = familyInfoService.findFamilyByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfoList);
        return "member/mypage";
    }

    // 회원 정보 수정
    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        Users user = usersService.findUserById(getLoggedInUser().getId());
        model.addAttribute("user", user);
        return "member/edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@ModelAttribute UsersRequest request) {
        Users user = usersService.findUserById(getLoggedInUser().getId());
        usersService.update(user.getId(), request);
        return "redirect:/mypage";
    }

    // 가족 정보 수정
    @GetMapping("/edit-family")
    public String editFamily(Model model) {
        Users user = usersService.findUserById(getLoggedInUser().getId());
        List<FamilyInfoResponse> familyInfoList = familyInfoService.findFamilyByUserId(null);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfoList);
        return "member/edit-family";
    }

    // 회원 탈퇴
    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "member/withdrawal";
    }

    @GetMapping("chat")
    public String chat() {
        return "chat/chat";
    }
}