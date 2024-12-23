package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanBaziService;
import com.example.classicjeans.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "앨런 오늘의 운세 api", description = "오늘의 운세")
@RestController
@RequestMapping("api")
public class AlanBaziController {
    private final AlanBaziService alanBaziService;

    public AlanBaziController(AlanBaziService alanBaziService) {
        this.alanBaziService = alanBaziService;
    }

    // 앨런 오늘의 운세
    @Operation(summary = "오늘의 운세 가져오기")
    @GetMapping("/bazi")
    public ResponseEntity<AlanBaziResponse> getOrCreateBaziResponse() {
        Users user = SecurityUtil.getLoggedInUser();
        Long userId = user.getId();

        // AlanBaziRequest 객체를 생성
        AlanBaziRequest request = new AlanBaziRequest();
        request.setBirthDate(user.getDateOfBirth());
        request.setGender(user.getGender().name());

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