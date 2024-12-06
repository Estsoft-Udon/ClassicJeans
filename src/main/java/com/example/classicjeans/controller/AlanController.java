package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanHealthRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.dto.response.AlanBasicResponse;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.example.classicjeans.dto.response.AlanQuestionnaireResponse;
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

    // 앨런 치매 문진표 질의
    @PostMapping("/api/analysis/dementia")
    public ResponseEntity<AlanDementiaResponse> getDementiaResponse(@RequestBody AlanDementiaRequest request) throws JsonProcessingException {
        AlanDementiaResponse response = alenService.fetchDementiaResponse(request);
        return ResponseEntity.ok(response);
    }
}
