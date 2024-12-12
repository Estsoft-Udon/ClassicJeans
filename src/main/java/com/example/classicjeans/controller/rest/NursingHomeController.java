package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.entity.NursingHomeData;
import com.example.classicjeans.service.NursingHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nursing")
public class NursingHomeController {

    private final NursingHomeService service;

    @GetMapping
    public ResponseEntity<List<NursingHomeResponse>> getNursingHome() {
        List<NursingHomeData> datas = service.getNursingHomeDatas();

        List<NursingHomeResponse> responses = datas.stream()
                .map(NursingHomeResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public void setNursingHomeDatas() throws Exception {
        service.setNursingHomeDatas();
    }
}