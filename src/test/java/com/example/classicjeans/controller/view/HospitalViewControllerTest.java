package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.service.HospitalService;
import com.example.classicjeans.util.SecurityUtil;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class HospitalViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HospitalService hospitalService;

    @InjectMocks
    private HospitalViewController controller;

    private static AutoCloseable closeable;

    UsersRequest request;
    Users user;

    @BeforeAll
    static void setUpClass() {
        closeable = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        closeable.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        request = new UsersRequest(
                "testLoginId", "testName", "testNickname", "testEmail@test.com",
                "testPassword", LocalDate.of(1990, 1, 1), false, 12, Gender.MALE, null
        );

        user = new Users(request);
        user.setId(1L);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        when(getLoggedInUser()).thenReturn(user);
    }
    @Test
    @DisplayName("기본 병원 목록 페이지 호출 테스트")
    void testGetHospitalList() throws Exception {
        // Given
        Page<HospitalResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(hospitalService.getAllHospitals(anyInt(), anyInt())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/hospital-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/hospital-list"))
                .andExpect(model().attributeExists("hospitals"))
                .andExpect(model().attribute("hospitals", mockPage.getContent()));
    }

    @Test
    @DisplayName("병원 이름 검색 테스트")
    void testSearchHospitalsByName() throws Exception {
        // Given
        Page<HospitalResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(hospitalService.searchHospitalsByName(anyString(), anyInt(), anyInt())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/hospital-list").param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/hospital-list"))
                .andExpect(model().attributeExists("hospitals"))
                .andExpect(model().attribute("hospitals", mockPage.getContent()))
                .andExpect(model().attribute("search", "test"));
    }

    @Test
    @DisplayName("지역 검색 테스트")
    void testSearchHospitalsByRegion() throws Exception {
        // Given
        Page<HospitalResponse> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(hospitalService.searchHospitals(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/hospital-list")
                        .param("city", "Seoul")
                        .param("district", "Gangnam"))
                .andExpect(status().isOk())
                .andExpect(view().name("info/hospital-list"))
                .andExpect(model().attributeExists("hospitals"))
                .andExpect(model().attribute("city", "Seoul"))
                .andExpect(model().attribute("district", "Gangnam"));
    }
}
