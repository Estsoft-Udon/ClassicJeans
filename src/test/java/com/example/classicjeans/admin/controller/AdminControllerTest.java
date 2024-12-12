package com.example.classicjeans.admin.controller;

import com.example.classicjeans.admin.service.AdminService;
import com.example.classicjeans.dto.response.UsersResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.enums.Grade;
import com.example.classicjeans.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private AdminController adminController;

    Users mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

        mockUser = new Users();
        mockUser.setId(1L);
        mockUser.setLoginId("testLoginId");
        mockUser.setName("testName");
        mockUser.setNickname("testNickname");
        mockUser.setEmail("testEmail");
        mockUser.setGrade(Grade.CHUNGBAZI);
        mockUser.setGender(Gender.MALE);
        mockUser.setPassword("testPassword");
        mockUser.setDateOfBirth(LocalDate.now());
        mockUser.setIsLunar(false);
        mockUser.setHourOfBirth(1);
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());
        mockUser.setUniqueKey(null);
    }

    @Test
    void testAdminMain() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-index"));
    }

    @Test
    void testMemberListForAdmin() throws Exception {
        // Given
        Page<Users> usersPage = new PageImpl<>(List.of(mockUser));
        when(adminService.getFilteredUsers(0, 10, "all", null)).thenReturn(usersPage);

        // When & Then
        mockMvc.perform(get("/admin/member/list")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortOption", "all"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/member/member-list"))
                .andExpect(model().attributeExists("allUser", "sortOption"));
    }

    @Test
    void testMemberDetailForAdmin() throws Exception {
        // Given
        when(usersService.findById(1L)).thenReturn(mockUser);

        // When & Then
        mockMvc.perform(get("/admin/member/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/member/member-edit"))
                .andExpect(model().attributeExists("user", "grades"));
    }

    @Test
    void testMemberEditForAdmin() throws Exception {
        // When & Then
        mockMvc.perform(post("/admin/member/edit/{id}", 1L)
                        .param("grade", Grade.ADMIN.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/member/edit/1"));

        // Verify service method was called
        verify(adminService, times(1)).updateUserGrade(1L, Grade.ADMIN);
    }

    @Test
    void testMemberDeleteForAdmin() throws Exception {
        // Given
        when(usersService.findById(1L)).thenReturn(mockUser);

        // When & Then
        mockMvc.perform(post("/admin/member/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/member/list"));

        // Verify service method was called
        verify(usersService, times(1)).softDelete(1L, mockUser.getPassword());
    }

    @Test
    void testMemberDeleteForAdmin_AdminUser() throws Exception {
        // Given
        Users user = mockUser;
        user.setGrade(Grade.ADMIN);
        when(usersService.findById(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/admin/member/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/member/list"));

        // Verify that softDelete was not called since it's an admin user
        verify(usersService, times(0)).softDelete(anyLong(), anyString());
    }
}
