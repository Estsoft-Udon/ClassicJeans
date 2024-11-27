package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanHealthRequest;
import com.example.classicjeans.dto.response.AlanBasicResponse;
import com.example.classicjeans.service.AlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
