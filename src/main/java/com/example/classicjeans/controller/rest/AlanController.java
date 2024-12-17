package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.example.classicjeans.dto.response.AlanQuestionnaireResponse;
import com.example.classicjeans.service.AlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "시니어 건강정보 api", description = "기본/치매 문진표")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class AlanController {

    private final AlanService alenService;

    // 앨런 기본 문진표 질의 (CLIENT_ID 바뀜에 따라 응답 값 확인)
    @Operation(summary = "기본 문진표 질의")
    @PostMapping("/questionnaire")
    public ResponseEntity<AlanQuestionnaireResponse> getQuestionnaireResponse(
            @RequestBody AlanQuestionnaireRequest request) throws JsonProcessingException {
        AlanQuestionnaireResponse response = alenService.fetchQuestionnaireResponse(request);
        return ResponseEntity.ok(response);
    }

    // 앨런 치매 문진표 질의 (CLIENT_ID 바뀜에 따라 응답 값 확인)
    @Operation(summary = "치매 문진표 질의")
    @PostMapping("/dementia")
    public ResponseEntity<AlanDementiaResponse> getDementiaResponse(@RequestBody AlanDementiaRequest request)
            throws JsonProcessingException {
        AlanDementiaResponse response = alenService.fetchDementiaResponse(request);
        return ResponseEntity.ok(response);
    }
}