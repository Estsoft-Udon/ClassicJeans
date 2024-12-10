package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.response.SanatoriumResponse;
import com.example.classicjeans.entity.SanatoriumData;
import com.example.classicjeans.service.SanatoriumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sanatorium")
public class SanatoriumController {

    private final SanatoriumService service;

    @GetMapping
    public ResponseEntity<List<SanatoriumResponse>> getSanatorium() {
        List<SanatoriumData> datas = service.getSanatoriumDatas();

        List<SanatoriumResponse> responses = datas.stream()
                .map(SanatoriumResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public void setSanatoriumDatas() throws Exception {
        service.setSanatoriumDatas();
    }
}