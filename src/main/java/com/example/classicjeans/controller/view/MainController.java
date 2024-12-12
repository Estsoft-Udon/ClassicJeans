package com.example.classicjeans.controller.view;

import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanBaziService;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    @RequestMapping("/")
    public String main() {
        return "index";
    }

    // 접근 제한
    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
