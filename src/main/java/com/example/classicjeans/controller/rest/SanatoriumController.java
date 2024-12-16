package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.response.SanatoriumResponse;
import com.example.classicjeans.entity.SanatoriumData;
import com.example.classicjeans.service.SanatoriumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "요양원 정보 api", description = "요양원 조회/저장")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sanatorium")
public class SanatoriumController {

    private final SanatoriumService service;

    @Operation(summary = "요양원 목록 조회")
    @GetMapping
    public ResponseEntity<List<SanatoriumResponse>> getSanatorium() {
        List<SanatoriumData> datas = service.getSanatoriumDatas();

        List<SanatoriumResponse> responses = datas.stream()
                .map(SanatoriumResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "요양원 목록 저장")
    @PostMapping
    public void setSanatoriumDatas() throws Exception {
        service.setSanatoriumDatas();
    }
}