package com.example.classicjeans.email.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testSaveAuthCode() {
        // Arrange
        String email = "test@example.com";
        String authCode = "123456";

        // Act
        authService.saveAuthCode(email, authCode);

        // Assert
        verify(valueOperations, times(1)).set(email, authCode, 5, TimeUnit.MINUTES);
    }

    @Test
    public void testValidateAuthCode_validCode() {
        // Arrange
        String email = "test@example.com";
        String authCode = "123456";
        when(valueOperations.get(email)).thenReturn(authCode);

        // Act
        boolean result = authService.validateAuthCode(email, authCode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testValidateAuthCode_invalidCode() {
        // Arrange
        String email = "test@example.com";
        String authCode = "123456";
        when(valueOperations.get(email)).thenReturn("654321");

        // Act
        boolean result = authService.validateAuthCode(email, authCode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testUpdateAuthStatus() {
        // Arrange
        String email = "test@example.com";

        // Act
        authService.updateAuthStatus(email);

        // Assert
        verify(valueOperations, times(1)).set("auth:email:" + email, "verified", Duration.ofHours(1));
    }

    @Test
    public void testIsEmailVerified_verified() {
        // Arrange
        String email = "test@example.com";
        when(valueOperations.get("auth:email:" + email)).thenReturn("verified");

        // Act
        boolean result = authService.isEmailVerified(email);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmailVerified_notVerified() {
        // Arrange
        String email = "test@example.com";
        when(valueOperations.get("auth:email:" + email)).thenReturn(null);

        // Act
        boolean result = authService.isEmailVerified(email);

        // Assert
        assertFalse(result);
    }
}
