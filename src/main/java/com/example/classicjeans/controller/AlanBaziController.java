package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanBaziService;
import com.example.classicjeans.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class AlanBaziController {
    private final AlanBaziService alanBaziService;

    public AlanBaziController(AlanBaziService alanBaziService) {
        this.alanBaziService = alanBaziService;
    }

    @PostMapping("/api/bazi/save")
    public Bazi saveBazi(
            @RequestParam Long userId,
            @RequestParam String birthDate, // LocalDate -> String 처리
            @RequestParam String gender) throws JsonProcessingException {

        AlanBaziRequest request = new AlanBaziRequest();
        request.setBirthDate(LocalDate.parse(birthDate));
        request.setGender(gender);

        return alanBaziService.saveBazi(userId, request);
    }

    // 앨런 오늘의 운세
    @GetMapping("/api/alan/bazi")
    public ResponseEntity<AlanBaziResponse> getOrCreateBaziResponse() {
        Users user = SecurityUtil.getLoggedInUser();
        Long userId = user.getId();

        // AlanBaziRequest 객체를 생성
        AlanBaziRequest request = new AlanBaziRequest();
        request.setBirthDate(user.getDateOfBirth());  // 사용자의 생일을 설정
        request.setGender(user.getGender().name());  // 사용자의 성별을 설정

        try {
            // AlanBaziRequest를 전달하여 운세 조회
            AlanBaziResponse response = alanBaziService.getOrCreateBazi(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            AlanBaziResponse errorResponse = new AlanBaziResponse();
            errorResponse.setContent("운세를 가져오는 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}

