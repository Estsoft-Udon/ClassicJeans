package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersService usersService;

    UsersRequest request;
    Users mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersService = new UsersService(usersRepository, passwordEncoder);

        request = new UsersRequest("testLoginId", "testName", "testNickName", "testEmail",
                "testPassword", LocalDate.now(), false, 5, Gender.MALE, null);

        mockUser = new Users(request);
        mockUser.setId(1L);
    }

    @Test
    void testRegister() {
        // given
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
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // when
        Users user = usersService.findUserById(1L);

        // then
        assertNotNull(user);
        assertEquals("testLoginId", user.getLoginId());

        mockUser.setIsDeleted(true);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        assertNull(usersService.findUserById(1L));
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
    void testUpdateUser() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser)); // 수정된 부분
        mockUser.setName("testUpdateName");

        // Mock 저장 동작 준비
        when(usersRepository.save(any(Users.class))).thenReturn(mockUser);

        // 서비스 호출
        Users result = usersService.update(1L, request);

        // 검증
        assertNotNull(result);
        assertEquals("testUpdateName", result.getName());
    }

    @Test
    void testSoftDelete() {
        // given
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
    void testSoftDeleteFail() {
        // given
        Long nonExistentUserId = 999L;
        when(usersRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // when
        Boolean result = usersService.softDelete(nonExistentUserId, "password");

        // then
        assertFalse(result);
    }

    @Test
    void testSoftDelete_incorrectPassword() {
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
    void testDelete() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        boolean result = usersService.delete(1L);
        assertTrue(result);
    }

    @Test
    void testSearchId() {
        // Given
        when(usersRepository.findByNameAndEmailAndIsDeletedFalse(anyString(), anyString()))
                .thenReturn(mockUser);

        // When
        Users result = usersService.searchId(request.getName(), request.getEmail());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(request.getName());
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    void testIsLoginCheckNickname() {
        when(usersRepository.existsByNicknameIgnoreCase(anyString())).thenReturn(true);

        boolean result = usersService.isLoginCheckNickname(request.getNickname());

        assertTrue(result);
    }


    @Test
    void testisLoginCheckEmail() {
        when(usersRepository.existsByEmailIgnoreCase(anyString())).thenReturn(true);

        boolean result = usersService.isLoginCheckEmail(request.getEmail());

        assertTrue(result);
    }

    @Test
    void testChangePassword() {
        // given
        Users mockUser2 = new Users(request);
        mockUser2.setPassword("encodedPassword");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser2));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // when
        boolean result = usersService.changePassword(1L, "password", "newPassword");

        // then
        assertTrue(result);
        assertEquals("encodedNewPassword", mockUser2.getPassword());
    }

    @Test
    void testChangePassword_incorrectCurrentPassword() {
        // given
        Users mockUser2 = new Users(request);
        mockUser2.setId(1L);
        mockUser2.setPassword("encodedPassword");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser2));
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

    @Test
    void testFindById() {
        when(usersRepository.findByIdAndIsDeletedFalse(anyLong())).thenReturn(mockUser);

        Users result = usersService.findById(1L);

        assertNotNull(result);
        assertEquals("testLoginId", result.getLoginId());
    }

    @Test
    void testChangePasswordAfterFind() {
        Users mockUser2 = new Users(request);
        when(usersRepository.findByLoginId(anyString())).thenReturn(mockUser);
        mockUser2.setPassword("updatedPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(mockUser2);
        assertThat(mockUser2.getPassword()).isEqualTo("updatedPassword");
    }

    @Test
    void testFindByLoginIdAndEmail() {
        when(usersRepository.findByLoginIdAndEmailAndIsDeletedFalse(anyString(), anyString())).thenReturn(mockUser);
        Users result = usersService.findByLoginIdAndEmail("testLoginId", "testEmail");
        assertNotNull(result);
        assertEquals("testLoginId", result.getLoginId());
    }
}