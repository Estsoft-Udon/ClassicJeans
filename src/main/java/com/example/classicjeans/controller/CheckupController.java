package com.example.classicjeans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class CheckupController {
    @RequestMapping("/checkout")
    public String checkout() {
        return "/checkout/checkout.html";
    }

    @RequestMapping("/checkout_list")
    public String checkoutist() {
        return "/checkout/checkout_list.html";
    }

    @RequestMapping("/questionnaire_list")
    public String questionnaireList() {
        return "/checkout/questionnaire_list.html";
    }

    @RequestMapping("/dementia_list")
    public String dementiaList() {
        return "/checkout/dementia_list.html";
    }

    @RequestMapping("/result")
    public String result() {
        return "/checkout/result.html";
    }
}
