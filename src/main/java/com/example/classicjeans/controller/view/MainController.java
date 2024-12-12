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

    @GetMapping("/")
    public String main(Model model) {
        // 인증된 사용자 정보 가져오기

        Users user = getLoggedInUser();
        if(user == null) {
            return "index";
        }

        model.addAttribute("nickname", user.getNickname());
        return "index";
    }

    // 접근 제한
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
