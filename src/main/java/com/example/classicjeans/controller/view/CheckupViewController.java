package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.dto.response.*;
import com.example.classicjeans.entity.*;
import com.example.classicjeans.service.*;
import com.example.classicjeans.util.MarkdownRenderer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckupViewController {
    private final UsersService usersService;
    private final FamilyInfoService familyInfoService;
    private final SessionUserService sessionUserService;
    private final AlanService alenService;
    private final HealthReportService healthReportService;

    // 건강 검진 메인
    @GetMapping
    public String checkout(Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfo);
        return "/checkout/checkout";
    }

    // 건강 검진 대상 선택
    @GetMapping("/checkout-list")
    public String checkoutist(@RequestParam(value = "selectedUser", required = false) String selectedUser,
                              @RequestParam(value = "selectedType", required = false) String selectedType,
                              HttpSession session) {

        if (selectedUser != null && selectedType != null) {
            if ("user".equals(selectedType)) {
                Long userId = getLoggedInUser().getId();
                Users selectedUserObj = usersService.findUserById(userId);
                session.setAttribute("selectedUser", selectedUserObj);
                session.setAttribute("selectedType", "user");
            } else if ("family".equals(selectedType)) {
                Long familyId = Long.parseLong(selectedUser);
                FamilyInfo selectedFamilyInfo = familyInfoService.findFamily(familyId);
                session.setAttribute("selectedUser", selectedFamilyInfo);
                session.setAttribute("selectedType", "family");
            }
        }
        return "/checkout/checkout-list";
    }

    // 기본 검사 페이지
    @GetMapping("/questionnaire-list")
    public String questionnaireList() {
        return "/checkout/questionnaire-list";
    }

    // 기본 검사 요청 기능
    @PostMapping("/questionnaire-list")
    public String questionnaireList(@ModelAttribute AlanQuestionnaireRequest request, HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Object selectedUserFromSession = session.getAttribute("selectedUser");
        String selectedTypeFromSession = (String) session.getAttribute("selectedType");

        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, request);
        redirectAttributes.addFlashAttribute("request", request);
        return "redirect:/checkout/result-questionnaire";
    }

    // 기본 검사 결과 페이지
    @GetMapping("/result-questionnaire")
    public String resultQuestionnaire(@ModelAttribute("request") AlanQuestionnaireRequest request, Model model,
                                      HttpSession session) throws JsonProcessingException {
        model.addAttribute("request", request);
        model.addAttribute("type", "questionnaire");
        AlanQuestionnaireResponse response = alenService.fetchQuestionnaireResponse(request);

        for (SummaryEvaluation evaluation : response.getSummaryEvaluation()) {
            evaluation.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(evaluation.getEvaluation()));
        }
        for (ImprovementSuggestions suggestion : response.getImprovementSuggestions()) {
            suggestion.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(suggestion.getSuggestion()));
        }
        model.addAttribute("response", response);

        session.removeAttribute("selectedUser");
        session.removeAttribute("selectedType");
        return "/checkout/result";
    }

    // 치매 검사 페이지
    @GetMapping("/dementia-list")
    public String dementiaList() {
        return "/checkout/dementia-list";
    }

    // 치매 검사 요청 기능
    @PostMapping("/dementia-list")
    public String dementiaList(@ModelAttribute AlanDementiaRequest request, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Object selectedUserFromSession = session.getAttribute("selectedUser");
        String selectedTypeFromSession = (String) session.getAttribute("selectedType");

        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, request);
        redirectAttributes.addFlashAttribute("dementiaRequest", request);
        return "redirect:/checkout/result-dementia";
    }

    // 치매 검사 결과 페이지
    @GetMapping("/result-dementia")
    public String resultDementia(@ModelAttribute("dementiaRequest") AlanDementiaRequest request, Model model,
                                 HttpSession session) throws JsonProcessingException {
        model.addAttribute("request", request);
        model.addAttribute("type", "dementia");
        AlanDementiaResponse response = alenService.fetchDementiaResponse(request);

        for (SummaryEvaluation evaluation : response.getSummaryEvaluation()) {
            evaluation.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(evaluation.getEvaluation()));
        }
        for (ImprovementSuggestions suggestion : response.getImprovementSuggestions()) {
            suggestion.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(suggestion.getSuggestion()));
        }
        model.addAttribute("response", response);

        session.removeAttribute("selectedUser");
        session.removeAttribute("selectedType");
        return "/checkout/result";
    }

    // 검사 결과 통계 페이지
    @GetMapping("/result-statistics")
    public String resultStatistics(Model model) {
        List<HealthStatisticsResponse> healthStatisticsList = healthReportService.getRecent5QuestionnaireData();
        model.addAttribute("healthStatisticsList", healthStatisticsList);
        return "/checkout/result-statistics";
    }

    // 검사 결과 목록 페이지
    @GetMapping("/result-list")
    public String resultList(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "all") String choiceUser,
                             Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        Page<HealthReportResponse> healthReportList = healthReportService.getHealthReportList(page, size, choiceUser);

        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfo);
        model.addAttribute("healthReportList", healthReportList);
        model.addAttribute("totalPages", healthReportList.getTotalPages());
        model.addAttribute("totalItems", healthReportList.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("choiceUser", choiceUser);

        return "/checkout/result-list";
    }

    // 검사 결과 상세 페이지(목록에서)
    @GetMapping("/result-detail/{reportId}")
    public String resultDetail(@PathVariable Long reportId,
                               @RequestParam String reportType,
                               Model model) {
        Object healthReport = healthReportService.getHealthReportById(reportId, reportType);

        if (healthReport instanceof QuestionnaireData questionnaireData) {
            for (SummaryEvaluation evaluation : questionnaireData.getSummaryEvaluation()) {
                evaluation.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(evaluation.getEvaluation()));
            }
            for (ImprovementSuggestions suggestion : questionnaireData.getImprovementSuggestions()) {
                suggestion.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(suggestion.getSuggestion()));
            }
            model.addAttribute("healthReport", questionnaireData);
        } else if (healthReport instanceof DementiaData dementiaData) {
            for (SummaryEvaluation evaluation : dementiaData.getSummaryEvaluation()) {
                evaluation.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(evaluation.getEvaluation()));
            }
            for (ImprovementSuggestions suggestion : dementiaData.getImprovementSuggestions()) {
                suggestion.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(suggestion.getSuggestion()));
            }
            model.addAttribute("healthReport", dementiaData);
        }

        model.addAttribute("reportType", reportType);
        return "/checkout/result-detail";
    }
}