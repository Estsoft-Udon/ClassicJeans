package com.example.classicjeans.admin.controller;

import com.example.classicjeans.admin.service.AdminService;
import com.example.classicjeans.admin.service.AdminMemberService;
import com.example.classicjeans.dto.response.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/member")
public class AdminMemberController {
//    private final AdminMemberService adminMemberService;
    private final AdminService adminService;
//    // 회원 목록
//    @GetMapping("/member_list")
//    public String boardListForAdmin(@RequestParam(defaultValue = "0") int page,
//                                    @RequestParam(defaultValue = "10") int size,
//                                    @RequestParam(defaultValue = "all") String sortOption,
//                                    @RequestParam(required = false) String keyword,
//                                    Model model) {
//        Page<UsersResponse> allUser = adminMemberService.getFilteredUsers(page, size, sortOption, keyword)
//                .map(UsersResponse::new);
//
//        model.addAttribute("allUser", allUser);
//        model.addAttribute("sortOption", sortOption);
//        model.addAttribute("keyword", keyword);
//        return "admin/member/member_list";
//    }
// 회원 목록
@GetMapping("/member_list")
public String boardListForAdmin() {
    return "admin/member/member_list";
}

//    // 회원 정보 수정(등급 수정)
//    @GetMapping("/member_edit/{id}")
//    public String boardEditForAdmin(Model model, @PathVariable Long id) {
//        UsersResponse userById = new UsersResponse(adminMemberService.getUserById(id));
//        model.addAttribute("grades", Grade.values());
//        model.addAttribute("user", userById);
//        return "admin/member/member_edit";
//    }
//
//    // 회원 정보 수정(등급 수정)
@GetMapping("/member_edit")
public String boardEditForAdmin() {
    return "admin/member/member_edit";
}
//    @PostMapping("/member_edit/{id}")
//    public String boardEditForAdmin(@PathVariable Long id, Grade grade) {
//        adminService.updateUserGrade(id, grade);
//        return "redirect:/admin/member/member_list";
//    }
}
