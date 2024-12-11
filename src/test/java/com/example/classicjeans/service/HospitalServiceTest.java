package com.example.classicjeans.service;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.entity.HospitalData;
import com.example.classicjeans.repository.HospitalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HospitalServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private HospitalService hospitalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllHospitals_ShouldReturnPageOfHospitalResponse() {
        // Given
        HospitalData hospitalData = new HospitalData("Hospital C", "City District", "111-222-3333", 37.568, 126.980, "City", "District");
        Page<HospitalData> hospitalPage = new PageImpl<>(Arrays.asList(hospitalData), PageRequest.of(0, 10), 1);

        when(hospitalRepository.findAll(any(Pageable.class))).thenReturn(hospitalPage);

        // When
        Page<HospitalResponse> hospitalResponsePage = hospitalService.getAllHospitals(0, 10);

        // Then
        assertNotNull(hospitalResponsePage);
        assertEquals(1, hospitalResponsePage.getTotalElements());
        assertEquals("Hospital C", hospitalResponsePage.getContent().get(0).getName());
    }

    @Test
    void searchHospitals_ShouldReturnPageOfHospitalResponse_WhenCityAndDistrictProvided() {
        // Given
        HospitalData hospitalData = new HospitalData("Hospital D", "City District", "444-555-6666", 37.569, 126.981, "City", "District");
        Page<HospitalData> hospitalPage = new PageImpl<>(Arrays.asList(hospitalData), PageRequest.of(0, 10), 1);

        when(hospitalRepository.findByCityAndDistrict(anyString(), anyString(), any(Pageable.class))).thenReturn(hospitalPage);

        // When
        Page<HospitalResponse> hospitalResponsePage = hospitalService.searchHospitals("City", "District", 0, 10);

        // Then
        assertNotNull(hospitalResponsePage);
        assertEquals(1, hospitalResponsePage.getTotalElements());
        assertEquals("Hospital D", hospitalResponsePage.getContent().get(0).getName());
    }

    @Test
    void searchHospitalsByName_ShouldReturnPageOfHospitalResponse() {
        // Given
        HospitalData hospitalData = new HospitalData("Hospital E", "City District", "777-888-9999", 37.570, 126.982, "City", "District");
        Page<HospitalData> hospitalPage = new PageImpl<>(Arrays.asList(hospitalData), PageRequest.of(0, 10), 1);

        when(hospitalRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(hospitalPage);

        // When
        Page<HospitalResponse> hospitalResponsePage = hospitalService.searchHospitalsByName("Hospital E", 0, 10);

        // Then
        assertNotNull(hospitalResponsePage);
        assertEquals(1, hospitalResponsePage.getTotalElements());
        assertEquals("Hospital E", hospitalResponsePage.getContent().get(0).getName());
    }
}