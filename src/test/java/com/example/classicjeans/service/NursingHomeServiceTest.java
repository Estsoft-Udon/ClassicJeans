package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.NursingHomeRequest;
import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.entity.NursingHomeData;
import com.example.classicjeans.repository.NursingHomeDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NursingHomeServiceTest {

    @InjectMocks
    private NursingHomeService nursingHomeService;

    @Mock
    private NursingHomeDataRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNursingHomeDatas_shouldReturnAllNursingHomeData() {
        // Given
        List<NursingHomeData> mockData = Collections.singletonList(new NursingHomeData());
        when(repository.findAll()).thenReturn(mockData);

        // When
        List<NursingHomeData> result = nursingHomeService.getNursingHomeDatas();

        // Then
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void parseJsonToDTOList_shouldReturnEmptyListWhenJsonIsNull() throws Exception {
        // When
        List<NursingHomeRequest> result = nursingHomeService.parseJsonToDTOList(null);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void getNursingHomeList_shouldReturnPagedResponse() {
        // Given
        Page<NursingHomeData> mockPage = new PageImpl<>(Collections.singletonList(new NursingHomeData()));
        when(repository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        // When
        Page<NursingHomeResponse> result = nursingHomeService.getNursingHomeList(PageRequest.of(0, 10));

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void getNursingHomeByRegion_shouldReturnPagedResponse() {
        // Given
        String region = "Seoul";
        String subregion = "Gangnam";
        Page<NursingHomeData> mockPage = new PageImpl<>(Collections.singletonList(new NursingHomeData()));
        when(repository.findAllByRegionAndSubRegion(eq(region), eq(subregion), any(PageRequest.class)))
                .thenReturn(mockPage);

        // When
        Page<NursingHomeResponse> result = nursingHomeService.getNursingHomeByRegion(PageRequest.of(0, 10), region, subregion);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAllByRegionAndSubRegion(eq(region), eq(subregion), any(PageRequest.class));
    }

    @Test
    void searchNursingHomeByName_shouldReturnPagedResponse() {
        // Given
        String search = "Test Nursing Home";
        Page<NursingHomeData> mockPage = new PageImpl<>(Collections.singletonList(new NursingHomeData()));
        when(repository.findAllByNameContaining(eq(search), any(PageRequest.class)))
                .thenReturn(mockPage);

        // When
        Page<NursingHomeResponse> result = nursingHomeService.searchNursingHomeByName(PageRequest.of(0, 10), search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAllByNameContaining(eq(search), any(PageRequest.class));
    }
}
