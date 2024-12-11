package com.example.classicjeans.security;

import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersDetailServiceTest {

    @InjectMocks
    private UsersDetailService usersDetailService;

    @Mock
    private UsersRepository usersRepository;

    private Users user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setId(1L);
        user.setLoginId("testuser");
        user.setEmail("testuser@example.com");
        user.setUniqueKey("uniqueKey123");
        user.setName("testuser");
        user.setIsDeleted(false);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testuser";
        when(usersRepository.findByLoginIdAndIsDeletedFalse(username)).thenReturn(user);

        // Act
        UserDetails userDetails = usersDetailService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getLoginId(), userDetails.getUsername());
        verify(usersRepository, times(1)).findByLoginIdAndIsDeletedFalse(username);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(usersRepository.findByLoginIdAndIsDeletedFalse(username)).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> usersDetailService.loadUserByUsername(username));
    }

    @Test
    public void testLoadUserByEmail_UserFound() {
        // Arrange
        String email = "testuser@example.com";
        when(usersRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails userDetails = usersDetailService.loadUserByEmail(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getName(), userDetails.getUsername());
        verify(usersRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByEmail_UserNotFound() {
        // Arrange
        String email = "nonexistentuser@example.com";
        when(usersRepository.findByEmail(email)).thenReturn(null);

        // Act
        UserDetails userDetails = usersDetailService.loadUserByEmail(email);

        // Assert
        assertNull(userDetails);
    }

    @Test
    public void testLoadUserByUniqueKey_UserFound() {
        // Arrange
        String uniqueKey = "uniqueKey123";
        when(usersRepository.findByUniqueKeyAndIsDeletedFalse(uniqueKey)).thenReturn(user);

        // Act
        UserDetails userDetails = usersDetailService.loadUserByUniqueKey(uniqueKey);

        // Assert
        assertNotNull(userDetails);
        verify(usersRepository, times(1)).findByUniqueKeyAndIsDeletedFalse(uniqueKey);
    }

    @Test
    public void testLoadUserByUniqueKey_UserNotFound() {
        // Arrange
        String uniqueKey = "nonexistentKey";
        when(usersRepository.findByUniqueKeyAndIsDeletedFalse(uniqueKey)).thenReturn(null);

        // Act
        UserDetails userDetails = usersDetailService.loadUserByUniqueKey(uniqueKey);

        // Assert
        assertNull(userDetails);
    }
}
