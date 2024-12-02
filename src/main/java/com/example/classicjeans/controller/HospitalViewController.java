package com.example.classicjeans.controller;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.entity.Hospital;
import com.example.classicjeans.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class HospitalViewController {

    @Autowired
    private HospitalService hospitalService;

    // 병원 목록 조회
    @GetMapping("/hospital_list")
    public String hospitalList(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "district", required = false) String district,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model) {
        Page<HospitalResponse> hospitalPage;

        // 검색어가 있을 경우 병원명으로 검색
        if (search != null && !search.isEmpty()) {
            hospitalPage = hospitalService.searchHospitalsByName(search, page, size);  // 병원명으로 검색
        } else if (city != null || district != null) {
            // city와 district가 있을 경우 검색
            hospitalPage = hospitalService.searchHospitals(city, district, page, size);
        } else {
            // 모든 병원 조회
            hospitalPage = hospitalService.getAllHospitals(page, size);
        }

        if (hospitalPage.getTotalElements() == 0) {
            model.addAttribute("noResults", true);
        }

        model.addAttribute("hospitals", hospitalPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hospitalPage.getTotalPages());
        model.addAttribute("city", city);
        model.addAttribute("district", district);
        model.addAttribute("size", size);
        model.addAttribute("search", search);

        return "/info/hospital_list";
    }

}
