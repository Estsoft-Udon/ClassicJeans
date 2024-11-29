package com.example.classicjeans.controller;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.service.HospitalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class HospitalViewController {
    private final HospitalService hospitalService;

    public HospitalViewController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    // 병원 목록
    @GetMapping("/hospital_list")
    public String getHospitalList(@RequestParam(defaultValue = "1") int pageNo, Model model) throws IOException, URISyntaxException {
        int numOfRows = 10;  // 한 페이지 항목 수
        List<HospitalResponse> hospitalList = hospitalService.getHospitalList(pageNo, numOfRows); // 병원 목록 조회
        int totalPages = hospitalService.getTotalPages(numOfRows); // 총 페이지 수

        model.addAttribute("hospitalList", hospitalList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNo);
        return "/info/hospital_list";  // Thymeleaf 템플릿 이름
    }


    // 지역 필터링된 병원 목록 (페이지네이션 적용)
    @GetMapping("/hospitals/filter")
    public String getFilteredHospitalList(@RequestParam String city, @RequestParam String district, @RequestParam(defaultValue = "1") int pageNo, Model model) throws IOException, URISyntaxException {
        int numOfRows = 10;  // 한 페이지 항목 수
        List<HospitalResponse> filteredHospitals = hospitalService.getFilteredHospitalList(city, district, pageNo, numOfRows); // 필터링된 병원 목록
        int totalPages = hospitalService.getTotalPages(numOfRows); // 총 페이지 수

        model.addAttribute("hospitalList", filteredHospitals);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNo);
        return "hospital :: #hospitalTable"; // Ajax 호출 후 테이블만 갱신
    }
}
