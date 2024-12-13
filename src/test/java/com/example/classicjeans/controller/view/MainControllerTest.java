package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.util.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static com.example.classicjeans.util.SecurityUtil.*;

class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    private UsersRequest request;
    private Users user;

    private static AutoCloseable closeable;

    @BeforeAll
    static void setUpClass() {
        closeable = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        closeable.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();

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
    void main_loggedInUser() throws Exception {
        when(getLoggedInUser()).thenReturn(user);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void main_notLoggedInUser() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        verifyNoInteractions(model);
    }

}
