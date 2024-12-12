package com.example.classicjeans.controller.rest;

import com.example.classicjeans.service.HospitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class HospitalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HospitalService hospitalService;

    @InjectMocks
    private HospitalController hospitalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hospitalController).build();
    }

//    @Test
//    @DisplayName("병원 목록 조회 테스트 (검색 및 페이지네이션 포함)")
//    void getHospitals_Success() throws Exception {
//        // Given
//        when(hospitalService.searchHospitals(any(), any(), anyInt(), anyInt()))
//                .thenReturn(Page.empty());  // mock으로 빈 페이지를 리턴
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/hospitals")
//                        .param("city", "Seoul")
//                        .param("district", "Gangnam")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())  // 상태 코드 200 OK
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty())  // 빈 페이지 반환
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("병원 전체 목록 저장 성공 테스트")
    void saveAllHospitals_Success() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/hospitals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())  // 상태 코드 200 OK
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    @DisplayName("병원명으로 병원 검색 성공 테스트")
//    void searchHospitalsByName_Success() throws Exception {
//        // Given
//        when(hospitalService.searchHospitalsByName(any(), anyInt(), anyInt()))
//                .thenReturn(Page.empty());  // mock으로 빈 페이지를 리턴
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/hospitals/search")
//                        .param("name", "HospitalName")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())  // 상태 코드 200 OK
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty())  // 빈 페이지 반환
//                .andDo(MockMvcResultHandlers.print());
//    }
}
