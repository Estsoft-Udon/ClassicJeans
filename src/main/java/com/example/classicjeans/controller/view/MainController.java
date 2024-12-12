package com.example.classicjeans.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String main() {
        return "index";
    }

    // 접근 제한
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
