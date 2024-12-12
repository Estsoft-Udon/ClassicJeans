package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.SanatoriumRequest;
import com.example.classicjeans.dto.response.SanatoriumResponse;
import com.example.classicjeans.entity.SanatoriumData;
import com.example.classicjeans.repository.SanatoriumDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SanatoriumServiceTest {

    @InjectMocks
    private SanatoriumService sanatoriumService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SanatoriumDataRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSanatoriumDatas_shouldReturnAllSanatoriumData() {
        // Given
        List<SanatoriumData> mockData = Collections.singletonList(new SanatoriumData());
        when(repository.findAll()).thenReturn(mockData);

        // When
        List<SanatoriumData> result = sanatoriumService.getSanatoriumDatas();

        // Then
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void fetchSanatoriumData_shouldReturnResponseBody() {
        // Given
        String expectedResponse = "mockResponse";
        String siDoCd = "11";
        int numOfRows = 10;
        when(restTemplate.exchange(any(), eq(org.springframework.http.HttpMethod.GET), eq(null), eq(String.class)))
                .thenReturn(new org.springframework.http.ResponseEntity<>(expectedResponse, org.springframework.http.HttpStatus.OK));

        // When
        String response = sanatoriumService.fetchSanatoriumData(siDoCd, numOfRows);

        // Then
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void parseJsonToDTOList_shouldReturnEmptyListWhenJsonIsNull() throws Exception {
        // When
        List<SanatoriumRequest> result = sanatoriumService.parseJsonToDTOList(null);

        // Then
        assertTrue(result.isEmpty());
    }


    @Test
    void getSanatoriumList_shouldReturnPagedResponse() {
        // Given
        Page<SanatoriumData> mockPage = new PageImpl<>(Collections.singletonList(new SanatoriumData()));
        when(repository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        // When
        Page<SanatoriumResponse> result = sanatoriumService.getSanatoriumList(PageRequest.of(0, 10));

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void searchSanatoriumByName_shouldReturnPagedResponse() {
        // Given
        String search = "Test";
        Page<SanatoriumData> mockPage = new PageImpl<>(Collections.singletonList(new SanatoriumData()));
        when(repository.findAllByNameContaining(eq(search), any(PageRequest.class))).thenReturn(mockPage);

        // When
        Page<SanatoriumResponse> result = sanatoriumService.searchSanatoriumByName(PageRequest.of(0, 10), search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAllByNameContaining(eq(search), any(PageRequest.class));
    }
}
