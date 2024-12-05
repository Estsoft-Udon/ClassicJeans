package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.FamilyInfoRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.service.FamilyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyInfoController {
    private final FamilyInfoService familyInfoService;

    // 가족 정보 저장 API
    @PostMapping
    public List<FamilyInfoResponse> saveFamily(@RequestBody List<FamilyInfoRequest> request) {
        return familyInfoService.saveFamily(null, request);
    }

    // 가족 정보 조회 API
    @GetMapping
    public List<FamilyInfoResponse> getFamilyByUserId(Long userId) {
        return familyInfoService.findFamilyByUserId(userId);
    }

    // 가족 정보 삭제 API
    @DeleteMapping("/{familyId}")
    public void deleteFamily(@PathVariable Long familyId) {
        familyInfoService.deleteFamily(familyId);
    }

}
