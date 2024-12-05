package com.example.classicjeans.controller;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    // 전체 병원 목록 저장
    @GetMapping("/saveAll")
    public ResponseEntity<String> saveAllHospitals() {
        try {
            hospitalService.saveAllHospitals(100);
            return ResponseEntity.ok("병원 목록이 DB에 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

    // 병원 목록 조회 (검색 및 페이지네이션 포함)
    @GetMapping
    public ResponseEntity<Page<HospitalResponse>> getHospitals(
            @RequestParam(required = false) String city,  // 검색: 도시
            @RequestParam(required = false) String district, // 검색: 구/시
            @RequestParam(defaultValue = "0") int page,      // 페이지 번호 (기본값 0)
            @RequestParam(defaultValue = "10") int size      // 페이지 크기 (기본값 10)
    ) {
        try {
            Page<HospitalResponse> hospitals = hospitalService.searchHospitals(city, district, page, size);
            return ResponseEntity.ok(hospitals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 병원명으로 병원 검색
    @GetMapping("/search")
    public Page<HospitalResponse> searchHospitalsByName(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size) {
        return hospitalService.searchHospitalsByName(name, page, size);
    }
}