package com.example.classicjeans.controller;

import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanService;
import com.example.classicjeans.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {


    private final AlanService alanService;
    private final UsersService usersService;

    public MainController(AlanService alanService, UsersService usersService) {
        this.alanService = alanService;
        this.usersService = usersService;
    }

    @RequestMapping("/")
    public String main(Model model) {
        // 예시로 3번째 사용자와 그에 해당하는 Bazi 데이터 가져오기
        Long userId = 3L;  // 예시로 사용자 ID를 3로 설정 (실제로는 동적으로 설정)

        Users user = usersService.findById(userId);
        Bazi bazi = alanService.getBaziByUserId(userId);

        // 모델에 데이터를 추가
        model.addAttribute("content", bazi.getContent());
        model.addAttribute("nickname", user.getNickname());
        return "index";
    }

    @RequestMapping("/chat")
    public String chat() {
        return "chat/chat";
    }

    // 접근 제한
    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

}
