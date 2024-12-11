package com.example.classicjeans.admin.service;

import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Grade;
import com.example.classicjeans.repository.UsersRepository;
import com.example.classicjeans.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    private Users user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setId(1L);
        user.setName("Test User");
        user.setGrade(Grade.CHUNGBAZI);
        user.setIsDeleted(false);
    }

    @Test
    public void testGetFilteredUsers_withKeyword() {
        // Arrange
        String keyword = "Test";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<Users> usersList = Collections.singletonList(user);
        Page<Users> usersPage = new PageImpl<>(usersList, pageable, usersList.size());

        when(usersRepository.findByNameContainingAndIsDeletedFalse(keyword, pageable)).thenReturn(usersPage);

        // Act
        Page<Users> result = adminService.getFilteredUsers(0, 10, "recent", keyword);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(usersRepository, times(1)).findByNameContainingAndIsDeletedFalse(keyword, pageable);
    }

    @Test
    public void testGetFilteredUsers_withoutKeyword() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<Users> usersList = Collections.singletonList(user);
        Page<Users> usersPage = new PageImpl<>(usersList, pageable, usersList.size());

        when(usersRepository.findAllByIsDeletedFalse(pageable)).thenReturn(usersPage);

        // Act
        Page<Users> result = adminService.getFilteredUsers(0, 10, "recent", "");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(usersRepository, times(1)).findAllByIsDeletedFalse(pageable);
    }

    @Test
    public void testUpdateUserGrade() {
        // Arrange
        Long userId = 1L;
        Grade newGrade = Grade.ADMIN;
        when(usersService.findById(userId)).thenReturn(user);

        // Act
        adminService.updateUserGrade(userId, newGrade);

        // Assert
        assertEquals(newGrade, user.getGrade());
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    public void testDetermineSortOrder_withRecentOption() {
        // Act
        Sort result = adminService.determineSortOrder("recent");

        // Assert
        assertEquals(Sort.by("createdAt").descending(), result);
    }

    @Test
    public void testDetermineSortOrder_withAbcOption() {
        // Act
        Sort result = adminService.determineSortOrder("abc");

        // Assert
        assertEquals(Sort.by("name").ascending(), result);
    }

    @Test
    public void testDetermineSortOrder_withDefaultOption() {
        // Act
        Sort result = adminService.determineSortOrder("default");

        // Assert
        assertEquals(Sort.unsorted(), result);
    }
}
