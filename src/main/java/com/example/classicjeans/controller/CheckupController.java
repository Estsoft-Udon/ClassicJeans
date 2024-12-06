package com.example.classicjeans.controller;

import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.FamilyInfoService;
import com.example.classicjeans.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckupController {
    private final UsersService usersService;
    private final FamilyInfoService familyInfoService;

    @RequestMapping("/checkout")
    public String checkout(Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfo);
        return "/checkout/checkout.html";
    }

    @RequestMapping("/checkout_list")
    public String checkoutist(@RequestParam(value = "selectedUser", required = false) String selectedUser,
                              @RequestParam(value = "selectedType", required = false) String selectedType,
                              HttpSession session) {

        if (selectedUser != null && selectedType != null) {
            if ("user".equals(selectedType)) {
                Long userId = getLoggedInUser().getId();
                Users selectedUserObj = usersService.findUserById(userId);
                System.out.println("Selected User (User Object): " + selectedUserObj);
                session.setAttribute("selectedUser", selectedUserObj);
                session.setAttribute("selectedType", "user");
            } else if ("family".equals(selectedType)) {
                Long familyId = Long.parseLong(selectedUser);
                FamilyInfo selectedFamilyInfo = familyInfoService.findFamily(familyId);
                session.setAttribute("selectedUser", selectedFamilyInfo);
                session.setAttribute("selectedType", "family");
            }
        }
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