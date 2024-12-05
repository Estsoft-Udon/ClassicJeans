package com.example.classicjeans.controller;

import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.FamilyInfoService;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
@SessionAttributes("selectedUser")
public class CheckupController {
    private final UsersService usersService;
    private final FamilyInfoService familyInfoService;

    @RequestMapping("/checkout")
    public String checkout(Model model, @RequestParam(value = "selectedUser", required = false) String selectedUser) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfo);
        model.addAttribute("selectedUser", selectedUser);
        return "/checkout/checkout.html";
    }

    @RequestMapping("/checkout_list")
    public String checkoutist() {
        return "/checkout/checkout_list.html";
    }

    @RequestMapping("/questionnaire_list")
    public String questionnaireList(Model model) {
        String selectedUser = (String) model.getAttribute("selectedUser");
        model.addAttribute("selectedUser", selectedUser);
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
