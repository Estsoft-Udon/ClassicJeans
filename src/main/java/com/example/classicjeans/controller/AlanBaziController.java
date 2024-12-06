package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.security.CustomUserDetails;
import com.example.classicjeans.service.AlanBaziService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class AlanBaziController {
    private final AlanBaziService alanBaziService;
    private final Map<String, LocalDateTime> userRequestMap = new ConcurrentHashMap<>();

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
    public ResponseEntity<?> getHealthResponse(
            @RequestParam(value = "birthDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> birthDate,
            @RequestParam(value = "gender", required = false) Optional<String> gender) throws JsonProcessingException {

        // 인증 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("Authenticated user: " + userDetails.getUsername());

        // 사용자 요청 제한 확인
        Long userId = userDetails.getUserId();  // Long으로 userId 가져오기
        LocalDateTime lastRequestTime = userRequestMap.get(userId.toString());  // userId를 문자열로 사용
        LocalDateTime now = LocalDateTime.now();

        if (lastRequestTime != null && Duration.between(lastRequestTime, now).toDays() < 1) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("하루에 한 번만 요청할 수 있습니다.");
        }

        // 사용자 정보 기본값 설정
        LocalDate finalBirthDate = birthDate.orElse(userDetails.getBirthDate() != null ? userDetails.getBirthDate() : LocalDate.of(1991, 9, 21));
        String finalGender = gender.orElseGet(() -> userDetails.getGender() != null ? userDetails.getGender().name() : "male");

        // AlanBazi 요청 생성
        AlanBaziRequest request = new AlanBaziRequest();
        request.setBirthDate(finalBirthDate);
        request.setGender(finalGender);

        try {
            // 운세 조회
            AlanBaziResponse response = alanBaziService.fetchBazi(request);

            // DB에 저장
            Bazi savedBazi = alanBaziService.saveBazi(userId, request);

            // 요청 기록 저장
            userRequestMap.put(userId.toString(), now);

            // 결과 반환
            return ResponseEntity.ok(savedBazi);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("운세 조회 중 오류가 발생했습니다.");
        }
    }


}

