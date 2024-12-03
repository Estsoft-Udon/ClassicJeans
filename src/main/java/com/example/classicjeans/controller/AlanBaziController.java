package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.service.AlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/bazi")
public class AlanBaziController {
    private final AlanService alanService;

    public AlanBaziController(AlanService alanService) {
        this.alanService = alanService;
    }

    @PostMapping("/save")
    public Bazi saveBazi(
            @RequestParam Long userId,
            @RequestParam String birthDate, // LocalDate -> String 처리
            @RequestParam String gender) throws JsonProcessingException {

        AlanBaziRequest request = new AlanBaziRequest();
        request.setBirthDate(LocalDate.parse(birthDate));
        request.setGender(gender);

        return alanService.saveBazi(userId, request);
    }
}
