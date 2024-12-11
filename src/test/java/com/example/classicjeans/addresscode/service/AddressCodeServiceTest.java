package com.example.classicjeans.addresscode.service;

import com.example.classicjeans.addresscode.CsvReader;
import com.example.classicjeans.addresscode.entity.AddressCode;
import com.example.classicjeans.addresscode.repository.AddressCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AddressCodeServiceTest {

    @Mock
    private AddressCodeRepository repository;

    @InjectMocks
    private AddressCodeService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAddress_Found() {
        // Mock 데이터
        AddressCode mockEntity = new AddressCode("code1", "address1");
        when(repository.findByCode("code1")).thenReturn(mockEntity);

        // 실행
        String result = service.getAddress("code1");

        // 검증
        assertEquals("address1", result);
        verify(repository, times(1)).findByCode("code1");
    }

    @Test
    void testGetAddress_NotFound() {
        // Mock 데이터: 없는 코드
        when(repository.findByCode("invalid-code")).thenReturn(null);

        // 실행
        String result = service.getAddress("invalid-code");

        // 검증
        assertEquals("", result);
        verify(repository, times(1)).findByCode("invalid-code");
    }
}
