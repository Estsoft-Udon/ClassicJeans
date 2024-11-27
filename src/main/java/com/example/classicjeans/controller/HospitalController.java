package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.HospitalRequest;
import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.service.HospitalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public List<HospitalResponse> getHospitals(
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam(required = false) String hospitalName) {

        HospitalRequest request = new HospitalRequest(city, district, hospitalName);

        return hospitalService.getHospitalList(request);
    }
}