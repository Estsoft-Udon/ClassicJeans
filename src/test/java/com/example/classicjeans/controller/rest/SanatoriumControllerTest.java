package com.example.classicjeans.controller.rest;

import com.example.classicjeans.entity.SanatoriumData;
import com.example.classicjeans.service.SanatoriumService;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class SanatoriumControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SanatoriumService service;

    @InjectMocks
    private SanatoriumController sanatoriumController;

    private List<SanatoriumData> sanatoriumDataList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sanatoriumController).build();

        sanatoriumDataList = new ArrayList<>();
        SanatoriumData data = new SanatoriumData();
        data.setId(1L);
        data.setName("TestName");
        data.setAddress("TestAddress");
        data.setRegion("TestRegion");
        data.setSubRegion("TestSubRegion");
        sanatoriumDataList.add(data);

    }

    @Test
    @DisplayName("Sanatorium 데이터 조회 성공 테스트")
    void getSanatorium_Success() throws Exception {
        // Given
        when(service.getSanatoriumDatas()).thenReturn(sanatoriumDataList);

        // When & Then
        mockMvc.perform(get("/api/sanatorium")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TestName"))
                .andExpect(jsonPath("$[0].address").value("TestAddress"));
    }

    @Test
    @DisplayName("Sanatorium 데이터 추가 성공 테스트")
    void setSanatoriumDatas_Success() throws Exception {
        // Given
        // No content expected, just a request to trigger service method
        doNothing().when(service).setSanatoriumDatas();

        // When & Then
        mockMvc.perform(post("/api/sanatorium")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
