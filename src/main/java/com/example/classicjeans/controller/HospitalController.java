package com.example.classicjeans.controller;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

//    // 병원 목록 API (페이지네이션 포함)
//    @GetMapping("/api/hospitals")
//    public List<HospitalResponse> getHospitals(
//            @RequestParam(required = false) String city,
//            @RequestParam(required = false) String district,
//            @RequestParam(defaultValue = "1") int pageNo,  // 기본 페이지 번호 1
//            @RequestParam(defaultValue = "10") int numOfRows  // 기본 항목 수 10
//    ) throws IOException, URISyntaxException {
//        if (city != null && district != null) {
//            // 특정 지역 검색
//            System.out.println("Filtering hospitals for city: " + city + ", district: " + district);
//            List<HospitalResponse> filteredHospitals = hospitalService.getFilteredHospitalList(city, district,pageNo, numOfRows);
//            System.out.println("Filtered hospitals count: " + filteredHospitals.size());
//            return filteredHospitals;
//        } else {
//            // 전체 병원 리스트 조회
//            System.out.println("Fetching all hospitals");
//            List<HospitalResponse> hospitalList = hospitalService.getHospitalList(pageNo, numOfRows);
//            System.out.println("Fetched hospitals count: " + hospitalList.size());
//            return hospitalList;
//        }
//    }
//
//    // 총 페이지 수 계산 API
//    @GetMapping("/api/hospitals/pages")
//    public int getTotalPages(
//            @RequestParam(defaultValue = "10") int numOfRows
//    ) throws IOException, URISyntaxException {
//        return hospitalService.getTotalPages(numOfRows);
//    }

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
}