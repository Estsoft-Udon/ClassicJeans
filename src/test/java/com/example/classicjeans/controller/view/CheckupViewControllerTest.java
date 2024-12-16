
package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.request.AlanDementiaRequest;

import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.HealthReportResponse;
import com.example.classicjeans.dto.response.HealthStatisticsResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.service.*;

import com.example.classicjeans.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.example.classicjeans.util.SecurityUtil.*;

public class CheckupViewControllerTest {

    @InjectMocks
    private CheckupViewController checkupViewController;

    @Mock
    private UsersService usersService;

    @Mock
    private FamilyInfoService familyInfoService;

    @Mock
    private SessionUserService sessionUserService;

    @Mock
    private AlanService alanService;

    @Mock
    private HealthReportService healthReportService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static AutoCloseable closeable;

    private  UsersRequest request;
    private Users user;

    private MockHttpSession session;

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
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkupViewController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

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
    public void testCheckout() throws Exception {
        when(usersService.findUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/checkout"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/checkout"))
                .andDo(result -> {
                    // ModelAndView로 검증
                    var mav = result.getModelAndView();
                    assertThat(mav).isNotNull();
                    assertThat(mav.getModel().get("user")).isEqualTo(user);
                });
    }


    @Test
    public void testCheckoutList() throws Exception {
        mockMvc.perform(get("/checkout/checkout-list")
                        .param("selectedUser", "1")
                        .param("selectedType", "user"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/checkout-list"))
                .andDo(result -> {
                    var mav = result.getModelAndView();
                    assertThat(mav).isNotNull();
                });
    }

    @Test
    public void testQuestionnaireListGet() throws Exception {
        mockMvc.perform(get("/checkout/questionnaire-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/questionnaire-list"));
    }

    @Test
    public void testQuestionnaireListPost() throws Exception {
        AlanQuestionnaireRequest request = new AlanQuestionnaireRequest();

        mockMvc.perform(post("/checkout/questionnaire-list")
                        .flashAttr("request", request))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/checkout/result-questionnaire"));
    }

//    @Test
//    public void testResultQuestionnaire() throws Exception {
//        AlanQuestionnaireRequest request = new AlanQuestionnaireRequest();
//        // 필요한 필드 설정
//
//        mockMvc.perform(get("/checkout/result-questionnaire")
//                        .flashAttr("request", request))
//                .andExpect(status().isOk())
//                .andExpect(view().name("checkout/result"));
//    }

    @Test
    public void testDementiaListGet() throws Exception {
        mockMvc.perform(get("/checkout/dementia-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/dementia-list"));
    }

    @Test
    public void testDementiaListPost() throws Exception {
        AlanDementiaRequest request = new AlanDementiaRequest();
        // 필요한 필드 설정

        mockMvc.perform(post("/checkout/dementia-list")
                        .flashAttr("request", request))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/checkout/result-dementia"));
    }

//    @Test
//    public void testResultDementia() throws Exception {
//        AlanDementiaRequest request = new AlanDementiaRequest();
//        // 필요한 필드 설정
//
//        mockMvc.perform(get("/checkout/result-dementia")
//                        .flashAttr("request", request))
//                .andExpect(status().isOk())
//                .andExpect(view().name("checkout/result"));
//    }

    @Test
    public void testResultStatistics() throws Exception {
        when(healthReportService.getRecent5QuestionnaireData()).thenReturn(List.of(new HealthStatisticsResponse()));
        when(healthReportService.getHealthReportList(anyInt(), anyInt(), anyString()))
                .thenReturn(new PageImpl<>(
                        Collections.singletonList(new HealthReportResponse()), // 콘텐츠 리스트
                        PageRequest.of(0, 10), // 페이지 정보
                        1 // 총 데이터 개수
                ));
        mockMvc.perform(get("/checkout/result-statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/result-statistics"));
    }

    @Test
    public void testResultList() throws Exception {
        Page<HealthReportResponse> healthReportList = new PageImpl<>(List.of(new HealthReportResponse()));
        when(healthReportService.getHealthReportList(anyInt(), anyInt(), anyString()))
                .thenReturn(healthReportList);

                mockMvc.perform(get("/checkout/result-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/result-list"));
    }

    @Test
    public void testResultDetail() throws Exception {
        mockMvc.perform(get("/checkout/result-detail/1")
                        .param("reportType", "questionnaire"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/result-detail"));
    }

}