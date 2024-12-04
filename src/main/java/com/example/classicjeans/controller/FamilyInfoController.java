package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.FamilyInfoRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.service.FamilyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyInfoController {
    private final FamilyInfoService familyInfoService;

    // 가족 정보 저장 API
    @PostMapping("/{userId}")
    public FamilyInfoResponse saveFamily(@PathVariable Long userId, @RequestBody FamilyInfoRequest request) {
        return familyInfoService.saveFamily(userId, request);
    }
}
