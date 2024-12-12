package com.example.classicjeans.admin.controller;

import com.example.classicjeans.admin.service.AdminService;
import com.example.classicjeans.dto.response.UsersResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Grade;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final UsersService usersService;

    @GetMapping
    public String adminMain() {
        return "admin/admin-index";
    }

    @GetMapping("/member/list")
    public String memberListForAdmin(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "all") String sortOption,
                                    @RequestParam(required = false) String keyword,
                                    Model model) {
        Page<UsersResponse> allUser = adminService.getFilteredUsers(page, size, sortOption, keyword)
                .map(UsersResponse::new);


        model.addAttribute("allUser", allUser);
        model.addAttribute("sortOption", sortOption);
        model.addAttribute("keyword", keyword);

        return "admin/member/member-list";
    }

    @GetMapping("/member/edit/{id}")
    public String memberDetailForAdmin(Model model, @PathVariable Long id) {
        UsersResponse userById = new UsersResponse(usersService.findById(id));
        model.addAttribute("user", userById);
        model.addAttribute("grades", Grade.values());

        return "admin/member/member-edit";
    }

    @PostMapping("/member/edit/{id}")
    public String memberEditForAdmin(@PathVariable Long id, Grade grade) {
        adminService.updateUserGrade(id, grade);
        return "redirect:/admin/member/edit/" + id;
    }

    @PostMapping("/member/delete/{id}")
    public String memberDeleteForAdmin(@PathVariable Long id) {
        Users user = usersService.findById(id);
        if(!user.getGrade().name().equals("ADMIN")) {
            usersService.softDelete(id, user.getPassword());
        }
        return "redirect:/admin/member/list";
    }
}