package com.example.classicjeans.controller;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.entity.Hospital;
import com.example.classicjeans.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
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
                               Model model) {
        List<HospitalResponse> hospitals;

        // city와 district가 있을 경우 검색
        if (city != null || district != null) {
            hospitals = hospitalService.searchHospitals(city, district);
        } else {
            hospitals = hospitalService.getAllHospitals(); // 모든 병원 목록 조회
        }

        model.addAttribute("hospitals", hospitals);
        return "/info/hospital_list";
    }

}
