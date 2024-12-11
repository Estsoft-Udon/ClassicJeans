package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.service.UsersService;
import com.example.classicjeans.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsersControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    UsersRequest request;
    Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockStatic(SecurityUtil.class);

        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜/시간 모듈 등록

        request = new UsersRequest(
                "testLoginId", "testName", "testNickname", "testEmail@test.com",
                "testPassword", LocalDate.of(1990, 1, 1), false, 12, Gender.MALE, null
        );

        user = new Users(request);
        user.setId(1L);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("유저 가입 성공 테스트")
    void register_Success() throws Exception {
        // Given
        Mockito.when(usersService.register(any(UsersRequest.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.loginId").value("testLoginId"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.nickname").value("testNickname"))
                .andExpect(jsonPath("$.email").value("testEmail@test.com"));
    }

    @Test
    @DisplayName("유저 조회 성공 테스트")
    void findUserById_Success() throws Exception {
        // Given
        Mockito.when(usersService.findUserById(eq(1L))).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.loginId").value("testLoginId"))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    void doWithdrawal_Success() throws Exception {
        // Given
        Mockito.when(usersService.softDelete(eq(1L), eq("password"))).thenReturn(true);
        Mockito.when(getLoggedInUser()).thenReturn(user);
        // When & Then
        mockMvc.perform(post("/api/users/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("password", "password"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 테스트 - 비밀번호 불일치")
    void doWithdrawal_Unauthorized() throws Exception {
        // Given
        Mockito.when(usersService.softDelete(eq(1L), eq("wrongPassword"))).thenReturn(false);
        Mockito.when(getLoggedInUser()).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/users/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("password", "wrongPassword"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("아이디 중복 확인 테스트")
    void checkId_Success() throws Exception {
        // Given
        Mockito.when(usersService.isLoginIdDuplicate(eq("testLoginId"))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/users/checkId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("loginId", "testLoginId"))))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("닉네임 중복 확인 테스트")
    void checkNickname_Success() throws Exception {
        // Given
        Mockito.when(usersService.isLoginCheckNickname(eq("testNickname"))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/users/checkNickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("nickname", "testNickname"))))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
