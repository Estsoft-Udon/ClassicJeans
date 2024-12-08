package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.dto.response.AlanQuestionnaireResponse;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.ImprovementSuggestions;
import com.example.classicjeans.entity.SummaryEvaluation;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanService;
import com.example.classicjeans.service.FamilyInfoService;
import com.example.classicjeans.service.SessionUserService;
import com.example.classicjeans.service.UsersService;
import com.example.classicjeans.util.MarkdownRenderer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckupController {
    private final UsersService usersService;
    private final FamilyInfoService familyInfoService;
    private final SessionUserService sessionUserService;
    private final AlanService alenService;

    @RequestMapping("/checkout")
    public String checkout(Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfo);
        return "checkout/checkout";
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
        return "checkout/checkout_list";
    }

    @GetMapping("/questionnaire_list")
    public String questionnaireList() {
        return "checkout/questionnaire_list";
    }

    @PostMapping("/questionnaire_list")
    public String questionnaireList(@ModelAttribute AlanQuestionnaireRequest request, HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Object selectedUserFromSession = session.getAttribute("selectedUser");
        String selectedTypeFromSession = (String) session.getAttribute("selectedType");

        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, request);
        redirectAttributes.addFlashAttribute("request", request);
        return "redirect:/checkout/result";
    }

    @GetMapping("/result")
    public String result(@ModelAttribute("request") AlanQuestionnaireRequest request, Model model) throws JsonProcessingException {
        model.addAttribute("request", request);
        AlanQuestionnaireResponse response = alenService.fetchQuestionnaireResponse(request);

        for (SummaryEvaluation evaluation : response.getSummaryEvaluation()) {
            evaluation.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(evaluation.getEvaluation()));
        }
        for (ImprovementSuggestions suggestion : response.getImprovementSuggestions()) {
            suggestion.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(suggestion.getSuggestion()));
        }
        model.addAttribute("response", response);
        return "checkout/result";
    }

    @GetMapping("/dementia_list")
    public String dementiaList() {
        return "checkout/dementia_list";
    }

    @PostMapping("/dementia_list")
    public String dementiaList(@ModelAttribute AlanDementiaRequest request, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Object selectedUserFromSession = session.getAttribute("selectedUser");
        String selectedTypeFromSession = (String) session.getAttribute("selectedType");

        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, request);
        redirectAttributes.addFlashAttribute("dementiaRequest", request);
        return "redirect:/checkout/result";
    }
}