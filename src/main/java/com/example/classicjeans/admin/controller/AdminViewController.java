package com.example.classicjeans.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {
    @GetMapping("/admin")
    public String adminMain() {
        return "admin/admin_index";
    }
}
