package com.example.classicjeans.controller;

import com.example.classicjeans.service.FamilyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyInfoController {
    private final FamilyInfoService familyInfoService;

}
