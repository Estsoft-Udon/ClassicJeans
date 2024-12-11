package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.FamilyInfoRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.service.FamilyInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FamilyInfoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FamilyInfoService familyInfoService;

    @InjectMocks
    private FamilyInfoController familyInfoController;

    private FamilyInfoRequest familyInfoRequest;
    private FamilyInfoResponse familyInfoResponse;

    ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(familyInfoController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜/시간 모듈 등록

        // 테스트 데이터 준비
        familyInfoRequest = new FamilyInfoRequest("TestName", Gender.MALE, LocalDate.now(), "Relationship");
        familyInfoResponse = new FamilyInfoResponse(1L, "TestName", Gender.MALE, LocalDate.now(), "RelationShip");
    }

    @Test
    @DisplayName("가족 정보 저장 테스트")
    public void saveFamily_Success() throws Exception {
        // Arrange
        List<FamilyInfoRequest> requestList = List.of(
                familyInfoRequest,
                familyInfoRequest
        );

        FamilyInfoResponse familyInfoResponse1 = familyInfoResponse;
        familyInfoResponse1.setId(2L);
        List<FamilyInfoResponse> responseList = List.of(familyInfoResponse, familyInfoResponse1);

        // Mocking the service call
        when(familyInfoService.saveFamily(null, requestList)).thenReturn(responseList);

        // Act & Assert
        mockMvc.perform(post("/api/family")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TestName"))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].relationship").value("RelationShip"))
                .andExpect(jsonPath("$[1].name").value("TestName"))
                .andExpect(jsonPath("$[1].gender").value("MALE"))
                .andExpect(jsonPath("$[1].relationship").value("RelationShip"));
    }

    @Test
    @DisplayName("가족 정보 조회 테스트")
    void getFamilyByUserId_Success() throws Exception {
        // Given
        List<FamilyInfoResponse> responses = Arrays.asList(familyInfoResponse);
        when(familyInfoService.findFamilyByUserId(1L)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/family?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TestName"))
                .andExpect(jsonPath("$[0].relationship").value("RelationShip"));
    }

    @Test
    @DisplayName("가족 정보 삭제 테스트")
    void deleteFamily_Success() throws Exception {
        // Given
        Long familyId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/family/{familyId}", familyId))
                .andExpect(status().isOk());
    }
}
