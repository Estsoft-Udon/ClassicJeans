package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersService = new UsersService(usersRepository, passwordEncoder);
    }

    public static UsersRequest tempRequest() {
        UsersRequest request = new UsersRequest("testLoginId", "testName", "testNickName", "testEmail",
                "testPassword", LocalDate.now(), false, 5, Gender.MALE, null);

        return request;
    }

    @Test
    void testRegister() {
        // given
        UsersRequest request = tempRequest();
        Users mockUser = new Users(request);
        mockUser.setId(1L);
        when(usersRepository.save(any(Users.class))).thenReturn(mockUser);

        // when
        Users result = usersService.register(request);

        // then
        assertNotNull(result);
        assertEquals("testLoginId", result.getLoginId());
    }

    @Test
    void testGetUsers() {
        // given
        UsersRequest request = tempRequest();

        Users user1 = new Users(request);
        Users user2 = new Users(request);
        when(usersRepository.findByIsDeletedFalse()).thenReturn(List.of(user1, user2));

        // when
        List<Users> users = usersService.getUsers();

        // then
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testFindUserById() {
        // given
        UsersRequest request = tempRequest();
        Users mockUser = new Users(request);
        mockUser.setId(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // when
        Users user = usersService.findUserById(1L);

        // then
        assertNotNull(user);
        assertEquals("testLoginId", user.getLoginId());
    }

    @Test
    void testFindUserById_notFound() {
        // given
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        Users user = usersService.findUserById(1L);

        // then
        assertNull(user);
    }

    @Test
    void testSoftDelete() {
        // given
        UsersRequest request = tempRequest();
        Users mockUser = new Users(request);
        mockUser.setId(1L);
        mockUser.setPassword("encodedPassword");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        // when
        boolean result = usersService.softDelete(1L, "password");

        // then
        assertTrue(result);
        assertTrue(mockUser.getIsDeleted());
    }

    @Test
    void testSoftDelete_incorrectPassword() {
        // given
        UsersRequest request = tempRequest();
        Users mockUser = new Users(request);
        mockUser.setId(1L);
        mockUser.setPassword("encodedPassword");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // when
        boolean result = usersService.softDelete(1L, "wrongPassword");

        // then
        assertFalse(result);
        assertFalse(mockUser.getIsDeleted());
    }

    @Test
    void testChangePassword() {
        // given
        UsersRequest request = tempRequest();
        Users mockUser = new Users(request);
        mockUser.setId(1L);
        mockUser.setPassword("encodedPassword");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // when
        boolean result = usersService.changePassword(1L, "password", "newPassword");

        // then
        assertTrue(result);
        assertEquals("encodedNewPassword", mockUser.getPassword());
    }

    @Test
    void testChangePassword_incorrectCurrentPassword() {
        // given
        UsersRequest request = tempRequest();
        Users mockUser = new Users(request);
        mockUser.setId(1L);
        mockUser.setPassword("encodedPassword");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // when
        boolean result = usersService.changePassword(1L, "wrongPassword", "newPassword");

        // then
        assertFalse(result);
    }

    @Test
    void testIsLoginIdDuplicate() {
        // given
        when(usersRepository.existsByLoginIdIgnoreCase("testLoginId")).thenReturn(true);

        // when
        boolean result = usersService.isLoginIdDuplicate("testLoginId");

        // then
        assertTrue(result);
    }
}