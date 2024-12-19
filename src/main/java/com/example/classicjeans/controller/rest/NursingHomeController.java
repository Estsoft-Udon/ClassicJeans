package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.entity.NursingHomeData;
import com.example.classicjeans.service.NursingHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "복지시설 정보 api", description = "복지시설 조회/저장")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nursing")
public class NursingHomeController {

    private final NursingHomeService service;

    @Operation(summary = "복지시설 목록 조회")
    @GetMapping
    public ResponseEntity<List<NursingHomeResponse>> getNursingHome() {
        List<NursingHomeData> datas = service.getNursingHomeDatas();

        List<NursingHomeResponse> responses = datas.stream()
                .map(NursingHomeResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "복지시설 목록 저장")
    @PostMapping
    public void setNursingHomeDatas() throws Exception {
        service.setNursingHomeDatas();
    }
}