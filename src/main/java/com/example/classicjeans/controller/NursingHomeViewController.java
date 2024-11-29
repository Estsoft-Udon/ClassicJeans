package com.example.classicjeans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NursingHomeViewController {

    @GetMapping("/nursing_list")
    public String nursingList() {
        return "/info/nursing_list";
    }

}
