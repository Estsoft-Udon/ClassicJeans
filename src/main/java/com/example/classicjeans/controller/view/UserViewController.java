package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.FamilyInfoService;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import static com.example.classicjeans.util.SecurityUtil.*;


@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UsersService usersService;
    private final FamilyInfoService familyInfoService;

    // 회원 정보
    @GetMapping("/mypage")
    public String myPage(Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);

        List<FamilyInfoResponse> familyInfoList = familyInfoService.findFamilyByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfoList);
        return "/member/mypage";
    }

    // 회원 정보 수정
    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        Users user = usersService.findUserById(getLoggedInUser().getId());
        model.addAttribute("user", user);
        return "/member/edit_profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@ModelAttribute UsersRequest request, Model model) {
        Users user = usersService.findUserById(getLoggedInUser().getId());
        usersService.update(user.getId(), request);
        return "redirect:/mypage";
    }

    // 가족정보수정
    @GetMapping("/edit_family")
    public String editFamily(Model model) {
        Users user = usersService.findUserById(getLoggedInUser().getId());
        List<FamilyInfoResponse> familyInfoList = familyInfoService.findFamilyByUserId(null);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfoList);
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
    public String signup() {

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
    public String findPw() {

        return "member/find_pw";
    }

    // 접근 제한
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
