package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SessionUserServiceTest {

    @InjectMocks
    private SessionUserService sessionUserService;

    @Mock
    private Users mockUser;

    @Mock
    private FamilyInfo mockFamilyInfo;

    @Mock
    private AlanQuestionnaireRequest alanQuestionnaireRequest;

    @Mock
    private AlanDementiaRequest alanDementiaRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void setUserFromSession_ShouldSetUserToAlanQuestionnaireRequest() {
        // Given
        String selectedTypeFromSession = "user";
        Object selectedUserFromSession = mockUser;

        // When
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, alanQuestionnaireRequest);

        // Then
        verify(alanQuestionnaireRequest, times(1)).setUser(mockUser);
    }

    @Test
    void setUserFromSession_ShouldSetUserToAlanDementiaRequest() {
        // Given
        String selectedTypeFromSession = "user";
        Object selectedUserFromSession = mockUser;

        // When
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, alanDementiaRequest);

        // Then
        verify(alanDementiaRequest, times(1)).setUser(mockUser);
    }

    @Test
    void setUserFromSession_ShouldSetFamilyToAlanQuestionnaireRequest() {
        // Given
        String selectedTypeFromSession = "family";
        Object selectedUserFromSession = mockFamilyInfo;

        // When
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, alanQuestionnaireRequest);

        // Then
        verify(alanQuestionnaireRequest, times(1)).setFamily(mockFamilyInfo);
    }

    @Test
    void setUserFromSession_ShouldSetFamilyToAlanDementiaRequest() {
        // Given
        String selectedTypeFromSession = "family";
        Object selectedUserFromSession = mockFamilyInfo;

        // When
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, alanDementiaRequest);

        // Then
        verify(alanDementiaRequest, times(1)).setFamily(mockFamilyInfo);
    }

    @Test
    void setUserFromSession_ShouldNotSetUserOrFamilyIfSelectedTypeIsInvalid() {
        // Given
        String selectedTypeFromSession = "invalid";
        Object selectedUserFromSession = mockFamilyInfo;

        // When
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, alanQuestionnaireRequest);

        // Then
        verify(alanQuestionnaireRequest, times(0)).setUser(any());
        verify(alanQuestionnaireRequest, times(0)).setFamily(any());
    }

    @Test
    void setUserFromSession_ShouldNotSetUserOrFamilyIfSelectedUserFromSessionIsNull() {
        // Given
        String selectedTypeFromSession = "user";
        Object selectedUserFromSession = null;

        // When
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, alanQuestionnaireRequest);

        // Then
        verify(alanQuestionnaireRequest, times(0)).setUser(any());
    }

    @Test
    void setUserFromSession_ShouldNotSetUserOrFamilyIfRequestIsNull() {
        // Given
        String selectedTypeFromSession = "user";
        Object selectedUserFromSession = mockUser;
        Object request = null;

        // When
        sessionUserService.setUserFromSession(selectedUserFromSession, selectedTypeFromSession, request);

        // Then
        // No exception and no interaction with mock objects.
    }
}
