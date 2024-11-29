package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanHealthRequest;
import com.example.classicjeans.dto.response.AlanBasicResponse;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.example.classicjeans.service.AlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AlanQuestionnaireResponse> getQuestionnaireResponse(@RequestBody AlanQuestionnaireRequest request) throws JsonProcessingException {
        AlanQuestionnaireResponse response = alenService.fetchQuestionnaireResponse(request);
        return ResponseEntity.ok(response);
    }

    // 앨런 오늘의 운세
    @GetMapping("/alan/bazi")
    public ResponseEntity<AlanBasicResponse> getHealthResponse() throws
            JsonProcessingException {
        AlanBasicResponse response = alenService.fetchBazi();
        return ResponseEntity.ok(response);
    }

    // 앨런 치매 문진표 질의
    @PostMapping("/api/analysis/dementia")
    public ResponseEntity<AlanDementiaResponse> getDementiaResponse(@RequestBody AlanDementiaRequest request) throws JsonProcessingException {
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
