package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.response.SanatoriumResponse;
import com.example.classicjeans.service.SanatoriumService;
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

class SanatoriumViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SanatoriumService service;

    @InjectMocks
    private SanatoriumViewController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("기본 요양원 목록 페이지 호출 테스트")
    void testGetSanatoriumList() throws Exception {
        // Given
        Page<SanatoriumResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(service.getSanatoriumList(any())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/sanatorium-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/sanatorium-list"))
                .andExpect(model().attributeExists("sanatoriums"))
                .andExpect(model().attribute("sanatoriums", mockPage));
    }

    @Test
    @DisplayName("이름 검색 테스트")
    void testSearchSanatoriumByName() throws Exception {
        // Given
        Page<SanatoriumResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(service.searchSanatoriumByName(any(), anyString())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/sanatorium-list").param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/sanatorium-list"))
                .andExpect(model().attributeExists("sanatoriums"))
                .andExpect(model().attribute("sanatoriums", mockPage))
                .andExpect(model().attribute("search", "test"));
    }

    @Test
    @DisplayName("지역 필터링 테스트")
    void testGetSanatoriumBySubregion() throws Exception {
        // Given
        Page<SanatoriumResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(service.getSanatoriumBySubregion(any(), anyString(), anyString())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/sanatorium-list")
                        .param("province", "Seoul")
                        .param("district", "Gangnam"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/sanatorium-list"))
                .andExpect(model().attributeExists("sanatoriums"))
                .andExpect(model().attribute("province", "Seoul"))
                .andExpect(model().attribute("district", "Gangnam"));
    }
}
