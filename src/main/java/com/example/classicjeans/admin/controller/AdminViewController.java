package com.example.classicjeans.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    // 관리자 메인
    @GetMapping
    public String adminMain() {
        return "admin/admin_index";
    }

    // 예약 목록
    @GetMapping("/reservation_list")
    public String reservationList() {
        return "admin/reservation/reservation_list";
    }

    // 예약 상세
    @GetMapping("/reservation_edit")
    public String reservationEdit() {
        return "admin/reservation/reservation_edit";
    }
}
