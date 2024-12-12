package com.example.classicjeans.controller.view;

import com.example.classicjeans.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import static com.example.classicjeans.util.SecurityUtil.*;

@Controller
@RequiredArgsConstructor
public class MainController {
    @RequestMapping("/")
    public String main() {
        return "index";
    }

    // 접근 제한
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
