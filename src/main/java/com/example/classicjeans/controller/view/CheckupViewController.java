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
    private final AlanService alanService;
    private final HealthReportService healthReportService;

    // 건강 검진 메인
    @GetMapping
    public String checkout(Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfo);
        return "checkout/checkout";
    }

    // 건강 검진 대상 선택
    @GetMapping("/checkout-list")
    public String checkoutList(@RequestParam(value = "selectedUser", required = false) String selectedUser,
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
        return "checkout/checkout-list";
    }

    // 기본 검사 페이지
    @GetMapping("/questionnaire-list")
    public String questionnaireList() {
        return "checkout/questionnaire-list";
    }

    // 기본 검사 요청 기능
    @PostMapping("/questionnaire-list")
    public String questionnaireList(@ModelAttribute AlanQuestionnaireRequest request, HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        processRequest(session, request);
        redirectAttributes.addFlashAttribute("request", request);
        return "redirect:/checkout/result-questionnaire";
    }

    // 기본 검사 결과 페이지
    @GetMapping("/result-questionnaire")
    public String resultQuestionnaire(@ModelAttribute("request") AlanQuestionnaireRequest request, Model model,
                                      HttpSession session) throws JsonProcessingException {
        if (session.getAttribute("request") == null) {
            session.setAttribute("request", request);
        } else {
            request = (AlanQuestionnaireRequest) session.getAttribute("request");
        }

        String type = "questionnaire";
        if (session.getAttribute("type") == null) {
            session.setAttribute("type", type);
        }

        Object response = session.getAttribute("response");
        if (response == null) {
            response = fetchResponse(request, type);
            session.setAttribute("response", response);
        }

        populateResultModel(request, model, type, session);
        return "checkout/result";
    }

    // 치매 검사 페이지
    @GetMapping("/dementia-list")
    public String dementiaList() {
        return "checkout/dementia-list";
    }

    // 치매 검사 요청 기능
    @PostMapping("/dementia-list")
    public String dementiaList(@ModelAttribute AlanDementiaRequest request, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        processRequest(session, request);
        redirectAttributes.addFlashAttribute("dementiaRequest", request);
        return "redirect:/checkout/result-dementia";
    }

    // 치매 검사 결과 페이지
    @GetMapping("/result-dementia")
    public String resultDementia(@ModelAttribute("dementiaRequest") AlanDementiaRequest request, Model model,
                                 HttpSession session) throws JsonProcessingException {
        if (session.getAttribute("dementiaRequest") == null) {
            session.setAttribute("dementiaRequest", request);
        } else {
            request = (AlanDementiaRequest) session.getAttribute("dementiaRequest");
        }

        String type = "dementia";
        if (session.getAttribute("type") == null) {
            session.setAttribute("type", type);
        }

        Object response = session.getAttribute("response");
        if (response == null) {
            response = fetchResponse(request, type);
            session.setAttribute("response", response);
        }

        populateResultModel(request, model, type, session);
        return "checkout/result";
    }

    // 검사 결과 통계 페이지
    @GetMapping("/result-statistics")
    public String resultStatistics(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam(defaultValue = "all") String choiceUser,
                                   Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        Page<HealthReportResponse> healthReportList = healthReportService.getHealthReportList(page, size, choiceUser);
        List<HealthStatisticsResponse> healthStatisticsList = healthReportService.getRecent5QuestionnaireData();

        model.addAttribute("healthStatisticsList", healthStatisticsList);
        addHealthReportPageAttributes(healthReportList, page, size, choiceUser, model, user, familyInfo);

        model.addAttribute("isStatisticsEmpty", healthStatisticsList.isEmpty());

        if (healthStatisticsList.size() == 1) {
            model.addAttribute("isSingleReport", true);
        } else if (healthStatisticsList.size() >= 2) {
            model.addAttribute("isMultipleReports", true);
        }

        return "checkout/result-statistics";
    }

    // 검사 결과 목록 페이지
    @GetMapping("/result-list")
    public String resultList(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "7") int size,
                             @RequestParam(defaultValue = "all") String choiceUser,
                             Model model) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfoResponse> familyInfo = familyInfoService.findFamilyByUserId(userId);
        Page<HealthReportResponse> healthReportList = healthReportService.getHealthReportList(page, size, choiceUser);

        addHealthReportPageAttributes(healthReportList, page, size, choiceUser, model, user, familyInfo);

        return "checkout/result-list";
    }

    // 검사 결과 상세 페이지(목록에서)
    @GetMapping("/result-detail/{reportId}")
    public String resultDetail(@PathVariable Long reportId,
                               @RequestParam String reportType,
                               Model model) {
        Object healthReport = healthReportService.getHealthReportById(reportId, reportType);

        if (healthReport instanceof QuestionnaireData questionnaireData) {
            processMarkdownContent(questionnaireData);
            model.addAttribute("healthReport", questionnaireData);
        } else if (healthReport instanceof DementiaData dementiaData) {
            processMarkdownContent(dementiaData);
            model.addAttribute("healthReport", dementiaData);
        }

        model.addAttribute("reportType", reportType);
        return "checkout/result-detail";
    }

    // 세션에 저장된 정보 가져오는 메소드
    private void processRequest(HttpSession session, Object request) {
        Object selectedUserFromSession = session.getAttribute("selectedUser");
        String selectedTypeFromSession = (String) session.getAttribute("selectedType");
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, request);
    }

    // 결과 페이지에 모델 채우기
    private void populateResultModel(Object request, Model model, String type, HttpSession session) throws JsonProcessingException {
        if ("questionnaire".equals(type)) {
            request = session.getAttribute("request");
            model.addAttribute("request", request);
        } else if ("dementia".equals(type)) {
            request = session.getAttribute("dementiaRequest");
            model.addAttribute("dementiaRequest", request);
        }

        model.addAttribute("type", type);

        Object response = session.getAttribute("response");
        if (response == null) {
            response = fetchResponse(request, type);
            session.setAttribute("response", response);
        }

        processMarkdownContent(response);
        model.addAttribute("response", response);
    }

    // 마크다운 변환 메소드
    private void processMarkdownContent(Object response) {
        if (response instanceof AlanQuestionnaireResponse data) {
            data.getSummaryEvaluation().forEach(e ->
                    e.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(e.getEvaluation())));
            data.getImprovementSuggestions().forEach(s ->
                    s.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(s.getSuggestion())));
        } else if (response instanceof AlanDementiaResponse data) {
            data.getSummaryEvaluation().forEach(e ->
                    e.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(e.getEvaluation())));
            data.getImprovementSuggestions().forEach(s ->
                    s.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(s.getSuggestion())));
        } else if (response instanceof QuestionnaireData data) {
            data.getSummaryEvaluation().forEach(e ->
                    e.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(e.getEvaluation())));
            data.getImprovementSuggestions().forEach(s ->
                    s.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(s.getSuggestion())));
        } else if (response instanceof DementiaData data) {
            data.getSummaryEvaluation().forEach(e ->
                    e.setEvaluation(MarkdownRenderer.convertMarkdownToHtml(e.getEvaluation())));
            data.getImprovementSuggestions().forEach(s ->
                    s.setSuggestion(MarkdownRenderer.convertMarkdownToHtml(s.getSuggestion())));
        }
    }

    // 요청 타입에 맞는 응답 데이터 가져오는 메소드
    private Object fetchResponse(Object request, String type) throws JsonProcessingException {
        return switch (type) {
            case "questionnaire" -> alanService.fetchQuestionnaireResponse((AlanQuestionnaireRequest) request);
            case "dementia" -> alanService.fetchDementiaResponse((AlanDementiaRequest) request);
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    // 검진 결과 목록 관련 정보 모델에 추가
    private void addHealthReportPageAttributes(Page<HealthReportResponse> healthReportList,
                                               int page, int size, String choiceUser,
                                               Model model, Users user,
                                               List<FamilyInfoResponse> familyInfo) {
        model.addAttribute("user", user);
        model.addAttribute("familyInfoList", familyInfo);
        model.addAttribute("healthReportList", healthReportList);
        model.addAttribute("totalPages", healthReportList.getTotalPages());
        model.addAttribute("totalItems", healthReportList.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("choiceUser", choiceUser);
    }
}