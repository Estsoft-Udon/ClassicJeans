package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.FamilyInfoRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.service.FamilyInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "가족정보 CRUD api", description = "가족정보 저장/조회/삭제")
@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyInfoController {
    private final FamilyInfoService familyInfoService;

    // 가족 정보 저장 API
    @Operation(summary = "가족 정보 저장")
    @PostMapping
    public List<FamilyInfoResponse> saveFamily(@RequestBody List<FamilyInfoRequest> request) {
        return familyInfoService.saveFamily(null, request);
    }

    // 가족 정보 조회 API
    @Operation(summary = "가족 정보 조회")
    @GetMapping
    public List<FamilyInfoResponse> getFamilyByUserId(Long userId) {
        return familyInfoService.findFamilyByUserId(userId);
    }

    // 가족 정보 삭제 API
    @Operation(summary = "가족 정보 삭제")
    @DeleteMapping("/{familyId}")
    public void deleteFamily(@PathVariable Long familyId) {
        familyInfoService.deleteFamily(familyId);
    }
}