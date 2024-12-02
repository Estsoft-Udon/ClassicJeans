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
    public String hospitalList(@RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "district", required = false) String district,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model) {
        Page<HospitalResponse> hospitalPage;

        // city와 district가 있을 경우 검색
        if (city != null || district != null) {
            hospitalPage = hospitalService.searchHospitals(city, district, page, size);
        } else {
            hospitalPage = hospitalService.getAllHospitals(page, size);
        }

        model.addAttribute("hospitals", hospitalPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hospitalPage.getTotalPages());
        model.addAttribute("city", city);
        model.addAttribute("district", district);
        model.addAttribute("size", size);

        return "/info/hospital_list";
    }

}
