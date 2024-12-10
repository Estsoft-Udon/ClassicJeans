package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.service.AlanBaziService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AlanBaziControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AlanBaziService alanBaziService;

    @InjectMocks
    private AlanBaziController alanBaziController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alanBaziController).build();
    }

    @Test
    void testGetHealthResponse() throws Exception {
        // AlanBaziRequest 객체 설정
        AlanBaziRequest request = new AlanBaziRequest();
        request.setBirthDate(LocalDate.of(1999, 12, 10));
        request.setGender("female");

        // AlanBaziResponse 객체 설정
        AlanBaziResponse response = new AlanBaziResponse();
        response.setContent("오늘의 운세: 좋은 기운이 있습니다.");
        response.setAction(new AlanBaziResponse.Action("bazi_action", "오늘은 운이 좋습니다."));

        // 서비스 메서드 모킹
        when(alanBaziService.fetchBazi(request)).thenReturn(response);

        // MockMvc를 사용한 GET 요청 수행
        mockMvc.perform(get("/alan/bazi")
                        .param("birthDate", "1999-12-10")
                        .param("gender", "female")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("오늘의 운세: 좋은 기운이 있습니다."))
                .andExpect(jsonPath("$.action.name").value("bazi_action"))
                .andExpect(jsonPath("$.action.speak").value("오늘은 운이 좋습니다."));
    }

}