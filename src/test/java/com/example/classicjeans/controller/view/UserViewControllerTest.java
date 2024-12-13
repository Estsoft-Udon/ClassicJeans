package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.email.service.AuthService;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.service.FamilyInfoService;
import com.example.classicjeans.service.UsersService;
import com.example.classicjeans.util.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UserViewControllerTest {

    @InjectMocks
    private UserViewController userViewController;

    @Mock
    private UsersService usersService;

    @Mock
    private AuthService authService;

    @Mock
    private FamilyInfoService familyInfoService;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    private MockHttpSession session;

    private static AutoCloseable closeable;

    UsersRequest request;
    Users user;

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
        mockMvc = MockMvcBuilders.standaloneSetup(userViewController).build();

        request = new UsersRequest(
                "testLoginId", "testName", "testNickname", "testEmail@test.com",
                "testPassword", LocalDate.of(1990, 1, 1), false, 12, Gender.MALE, null
        );

        user = new Users(request);
        user.setId(1L);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        session = new MockHttpSession();
        when(getLoggedInUser()).thenReturn(user);
    }

    @Test
    void login_withError() throws Exception {
        // Arrange
        String errorMessage = "Invalid credentials";
        session.setAttribute("error", errorMessage);

        // Act & Assert
        mockMvc.perform(get("/login").param("error", "true").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("member/login"))
                .andDo(result -> {
                    // ModelAndView로 검증
                    var mav = result.getModelAndView();
                    assertThat(mav).isNotNull();
                    assertThat(mav.getModel().get("error")).isEqualTo(errorMessage);
                });
    }

    @Test
    void signup_successful() throws Exception {
        when(authService.isEmailVerified(any())).thenReturn(true);
        when(usersService.register(any(UsersRequest.class))).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/signup")
                        .flashAttr("request", request)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/success"));
    }

    @Test
    void signup_withUnverifiedEmail() throws Exception {
        // Arrange
        when(authService.isEmailVerified("testEmail")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/signup")
                        .flashAttr("request", request)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("member/signup"));
    }

    @Test
    void findId_success() throws Exception {
        // Arrange
        when(usersService.searchId("testName", "testEmail")).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/find-id")
                        .param("name", "testName")
                        .param("email", "testEmail"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/find-id"));
    }

    @Test
    void findId_notFound() throws Exception {
        // Arrange
        when(usersService.searchId("testName", "testEmail")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/find-id")
                        .param("name", "testName")
                        .param("email", "testEmail"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/find-id"));}

    @Test
    void myPage() throws Exception {
        when(usersService.findUserById(1L)).thenReturn(user);
        when(familyInfoService.findFamilyByUserId(1L)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/mypage"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/mypage"));
    }
}
