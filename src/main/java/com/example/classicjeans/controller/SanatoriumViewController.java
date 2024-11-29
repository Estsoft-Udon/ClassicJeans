package com.example.classicjeans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanatoriumViewController {

    @GetMapping("/sanatorium_list")
    public String sanatoriumList() {
        return "/info/sanatorium_list";
    }
}
