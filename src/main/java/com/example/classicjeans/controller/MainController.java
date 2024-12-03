package com.example.classicjeans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String main() {
        return "index";
    }

    @RequestMapping("/chat")
    public String chat() {
        return "chat/chat";
    }
}
