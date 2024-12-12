package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.service.NursingHomeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class NursingHomeViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NursingHomeService service;

    @InjectMocks
    private NursingHomeViewController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("기본 간호 목록 페이지 호출 테스트")
    void testGetNursingList() throws Exception {
        // Given
        Page<NursingHomeResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(service.getNursingHomeList(any())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/nursing-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/nursing-list"))
                .andExpect(model().attributeExists("nursinghomes"))
                .andExpect(model().attribute("nursinghomes", mockPage));
    }

    @Test
    @DisplayName("이름 검색 테스트")
    void testSearchNursingHomeByName() throws Exception {
        // Given
        Page<NursingHomeResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(service.searchNursingHomeByName(any(), anyString())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/nursing-list").param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/nursing-list"))
                .andExpect(model().attributeExists("nursinghomes"))
                .andExpect(model().attribute("nursinghomes", mockPage))
                .andExpect(model().attribute("search", "test"));
    }

    @Test
    @DisplayName("지역 필터링 테스트")
    void testGetNursingHomeByRegion() throws Exception {
        // Given
        Page<NursingHomeResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(service.getNursingHomeByRegion(any(), anyString(), anyString())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/nursing-list")
                        .param("province", "Seoul")
                        .param("district", "Gangnam"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/nursing-list"))
                .andExpect(model().attributeExists("nursinghomes"))
                .andExpect(model().attribute("province", "Seoul"))
                .andExpect(model().attribute("district", "Gangnam"));
    }
}
