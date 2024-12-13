package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.FamilyInfoRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.repository.FamilyInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FamilyInfoServiceTest {

    @Mock
    private UsersService usersService;

    @Mock
    private FamilyInfoRepository familyInfoRepository;

    @InjectMocks
    private FamilyInfoService familyInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    FamilyInfoRequest tempFamilyInfoRequest() {
        FamilyInfoRequest familyInfoRequest = new FamilyInfoRequest("John Doe", Gender.MALE, LocalDate.now(), false, "Brother");
        return familyInfoRequest;
    }

    FamilyInfo tempFamilyInfo() {
        Users user = new Users();
        user.setId(1L);

        FamilyInfo familyInfo = new FamilyInfo();
        familyInfo.setId(1L);
        familyInfo.setUserId(user);
        familyInfo.setName("John Doe");
        familyInfo.setGender(Gender.MALE);
        familyInfo.setDateOfBirth(LocalDate.now());
        familyInfo.setRelationship("Brother");
        return familyInfo;
    }

    @Test
    void saveFamily_ShouldSaveFamilyInfo() {
        // Given
        Long userId = 1L;
        Users user = new Users();
        user.setId(userId);

        FamilyInfoRequest familyInfoRequest = tempFamilyInfoRequest();
        List<FamilyInfoRequest> requests = Arrays.asList(familyInfoRequest);

        FamilyInfo familyInfo = tempFamilyInfo();

        when(usersService.findUserById(userId)).thenReturn(user);
        when(familyInfoRepository.save(any(FamilyInfo.class))).thenReturn(familyInfo);

        // When
        List<FamilyInfoResponse> response = familyInfoService.saveFamily(userId, requests);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).getName());
        assertEquals(Gender.MALE, response.get(0).getGender());
        assertEquals(LocalDate.now(), response.get(0).getDateOfBirth());
        assertEquals("Brother", response.get(0).getRelationship());
    }

    @Test
    void findFamilyByUserId_ShouldReturnFamilyInfoList() {
        // Given
        Long userId = 1L;
        Users user = new Users();
        user.setId(userId);

        FamilyInfo familyInfo = tempFamilyInfo();

        when(usersService.findUserById(userId)).thenReturn(user);
        when(familyInfoRepository.findByUserId(user)).thenReturn(Arrays.asList(familyInfo));

        // When
        List<FamilyInfoResponse> response = familyInfoService.findFamilyByUserId(userId);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).getName());
        assertEquals(Gender.MALE, response.get(0).getGender());
        assertEquals(LocalDate.now(), response.get(0).getDateOfBirth());
        assertEquals("Brother", response.get(0).getRelationship());
    }

    @Test
    void findFamily_ShouldReturnFamilyInfo() {
        // Given
        Long familyId = 1L;
        FamilyInfo familyInfo = new FamilyInfo();
        familyInfo.setId(familyId);

        when(familyInfoRepository.findById(familyId)).thenReturn(java.util.Optional.of(familyInfo));

        // When
        FamilyInfo result = familyInfoService.findFamily(familyId);

        // Then
        assertNotNull(result);
        assertEquals(familyId, result.getId());
    }

    @Test
    void findFamily_ShouldReturnNullIfNotFound() {
        // Given
        Long familyId = 1L;

        when(familyInfoRepository.findById(familyId)).thenReturn(java.util.Optional.empty());

        // When
        FamilyInfo result = familyInfoService.findFamily(familyId);

        // Then
        assertNull(result);
    }

    @Test
    void deleteFamily_ShouldDeleteFamilyInfo() {
        // Given
        Long familyId = 1L;
        FamilyInfo familyInfo = new FamilyInfo();
        familyInfo.setId(familyId);

        when(familyInfoRepository.findById(familyId)).thenReturn(java.util.Optional.of(familyInfo));

        // When
        familyInfoService.deleteFamily(familyId);

        // Then
        verify(familyInfoRepository, times(1)).delete(familyInfo);
    }

    @Test
    void deleteFamily_ShouldThrowExceptionIfFamilyNotFound() {
        // Given
        Long familyId = 1L;

        when(familyInfoRepository.findById(familyId)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> familyInfoService.deleteFamily(familyId));
    }
}
