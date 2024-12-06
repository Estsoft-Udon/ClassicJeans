package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanHealthRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.dto.response.AlanBasicResponse;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.example.classicjeans.dto.response.AlanQuestionnaireResponse;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.SourceVersion;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AlanController {

    private final AlanService alenService;

    // 앨런 기본 질의
    @GetMapping("/alan/basic")
    public ResponseEntity<AlanBasicResponse> getAlanResponse(@RequestParam String content) throws
            JsonProcessingException {
        AlanBasicResponse response = alenService.fetchBasicResponse(content);
        return ResponseEntity.ok(response);
    }

    // 앨런 기본 질의
    @GetMapping("/alan/health")
    public ResponseEntity<AlanBasicResponse> getHealthResponse(@RequestBody AlanHealthRequest request) throws
            JsonProcessingException {
        AlanBasicResponse response = alenService.fetchHealthResponse(request);
        return ResponseEntity.ok(response);
    }

    // 앨런 기본 문진표 질의
    @PostMapping("/api/analysis/questionnaire")
    public ResponseEntity<AlanQuestionnaireResponse> getQuestionnaireResponse(@RequestBody AlanQuestionnaireRequest request, HttpSession session) throws JsonProcessingException {
        Object selectedUserFromSession = session.getAttribute("selectedUser");
        String selectedTypeFromSession = (String) session.getAttribute("selectedType");

        // 세션에 저장된 객체가 null이 아니고, selectedType이 존재하는지 확인
        if (selectedUserFromSession != null && selectedTypeFromSession != null) {
            if ("user".equals(selectedTypeFromSession)) {
                Users selectedUser = (Users) selectedUserFromSession;
                request.setUser(selectedUser);
            } else if ("family".equals(selectedTypeFromSession)) {
                FamilyInfo selectedFamilyInfo = (FamilyInfo) selectedUserFromSession;
                request.setFamily(selectedFamilyInfo);
            }
        }
        AlanQuestionnaireResponse response = alenService.fetchQuestionnaireResponse(request);
        return ResponseEntity.ok(response);
    }

    // 앨런 치매 문진표 질의
    @PostMapping("/api/analysis/dementia")
    public ResponseEntity<AlanDementiaResponse> getDementiaResponse(@ModelAttribute AlanDementiaRequest request, HttpSession session) throws JsonProcessingException {
        Object selectedUserFromSession = session.getAttribute("selectedUser");
        String selectedTypeFromSession = (String) session.getAttribute("selectedType");

        // 세션에 저장된 객체가 null이 아니고, selectedType이 존재하는지 확인
        if (selectedUserFromSession != null && selectedTypeFromSession != null) {
            if ("user".equals(selectedTypeFromSession)) {
                Users selectedUser = (Users) selectedUserFromSession;
                request.setUser(selectedUser);
            } else if ("family".equals(selectedTypeFromSession)) {
                FamilyInfo selectedFamilyInfo = (FamilyInfo) selectedUserFromSession;
                request.setFamily(selectedFamilyInfo);
            }
        }
        AlanDementiaResponse response = alenService.fetchDementiaResponse(request);
        return ResponseEntity.ok(response);
    }

    // 앨런 오늘의 운세
    @GetMapping("/alan/bazi")
    public ResponseEntity<AlanBaziResponse> getHealthResponse(
            @RequestParam(value = "birthDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> birthDate,
            @RequestParam(value = "gender", required = false) Optional<String> gender) throws JsonProcessingException {

        // 임의의 기본값 설정 - 추후 삭제
        LocalDate finalBirthDate = birthDate.orElse(LocalDate.of(1999, 12, 10));
        String finalGender = gender.orElse("female"); // 기본값 설정

        // AlanBaziRequest 객체 생성 및 설정
        AlanBaziRequest request = new AlanBaziRequest();
        request.setBirthDate(finalBirthDate);
        request.setGender(finalGender);

        // 운세 조회
        AlanBaziResponse response = alenService.fetchBazi(request);
        return ResponseEntity.ok(response);
    }
}
