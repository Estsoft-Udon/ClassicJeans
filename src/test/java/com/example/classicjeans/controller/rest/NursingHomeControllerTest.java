package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.entity.NursingHomeData;
import com.example.classicjeans.service.NursingHomeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class NursingHomeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NursingHomeService service;

    private List<NursingHomeData> nursingHomeDataList;

    @InjectMocks
    private NursingHomeController nursingHomeController;

    @BeforeEach
    void setUp() {
        // Test data
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(nursingHomeController).build();


        nursingHomeDataList = new ArrayList<>();
        NursingHomeData data = new NursingHomeData();
        data.setId(1L);
        data.setName("TestName");
        data.setAddress("TestAddress");
        data.setPhone("TestPhone");
        nursingHomeDataList.add(data);
    }

    @Test
    @DisplayName("Nursing Home 데이터 조회 성공 테스트")
    void getNursingHome_Success() throws Exception {
        // Given
        when(service.getNursingHomeDatas()).thenReturn(nursingHomeDataList);

        // When & Then
        mockMvc.perform(get("/api/nursing")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TestName"))
                .andExpect(jsonPath("$[0].address").value("TestAddress"));
    }

    @Test
    @DisplayName("Nursing Home 데이터 추가 성공 테스트")
    void setNursingHomeDatas_Success() throws Exception {
        // Given
        // No content expected, just a request to trigger service method
        doNothing().when(service).setNursingHomeDatas();

        // When & Then
        mockMvc.perform(post("/api/nursing")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
