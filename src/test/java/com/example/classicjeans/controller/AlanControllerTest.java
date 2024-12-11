//package com.example.classicjeans.controller;
//
//import com.example.classicjeans.controller.rest.AlanController;
//import com.example.classicjeans.dto.request.AlanDementiaRequest;
//import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
//import com.example.classicjeans.dto.response.AlanDementiaResponse;
//import com.example.classicjeans.dto.response.AlanQuestionnaireResponse;
//import com.example.classicjeans.enums.questionnaire.*;
//import com.example.classicjeans.service.AlanService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Arrays;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//class AlanControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private AlanService alanService;
//
//    @InjectMocks
//    private AlanController alanController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(alanController).build();
//    }
//
//    // 기본 검사 테스트
//    @Test
//    void testGetQuestionnaireResponse() throws Exception {
//        AlanQuestionnaireRequest request = new AlanQuestionnaireRequest();
//        request.setHeight(160.0);
//        request.setWeight(85.0);
//        request.setChronicDisease(ChronicDisease.DIABETES);
//        request.setHospitalVisit(HospitalVisit.DISEASE_TREATMENT);
//        request.setCurrentMedication(Medication.DIABETES_MEDICINE);
//        request.setSmokingStatus(SmokingStatus.CURRENTLY_SMOKING);
//        request.setAlcoholConsumption(AlcoholConsumption.REGULAR);
//        request.setExerciseFrequency(ExerciseFrequency.NONE);
//        request.setDietPattern(DietPattern.IRREGULAR_MEALS);
//        request.setMoodStatus(MoodStatus.PERSISTENTLY_ANXIOUS_OR_DEPRESSED);
//        request.setSleepPattern(SleepPattern.INSUFFICIENT);
//        request.setIndependenceLevel(IndependenceLevel.PARTIALLY_DEPENDENT);
//        request.setSocialParticipation(SocialParticipation.NONE);
//        request.setHasGeneticDisease(true);
//        request.setWeightChange(WeightChange.WEIGHT_GAIN);
//        request.setHasAllergy(true);
//
//        AlanQuestionnaireResponse response = new AlanQuestionnaireResponse();
//        response.setAction(new AlanQuestionnaireResponse.Action("name", "speak"));
//        response.setContent("질문에 대한 응답 내용");
//        response.setAgeGroup("40대");
//        response.setAverageHeight(175.0);
//        response.setAverageWeight(68.0);
//        response.setSmokingRate(30.0);
//        response.setDrinkingRate(40.0);
//        response.setExerciseRate(50.0);
//        response.setSummaryEvaluation(Arrays.asList("Evaluation 1", "Evaluation 2"));
//        response.setImprovementSuggestions(Arrays.asList("Suggestion 1", "Suggestion 2"));
//
//        // 서비스 메서드를 모킹
//        when(alanService.fetchQuestionnaireResponse(request)).thenReturn(response);
//
//        // MockMvc를 사용한 POST 요청 수행
//        mockMvc.perform(post("/api/analysis/questionnaire")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.action.name").value("name"))
//                .andExpect(jsonPath("$.action.speak").value("speak"))
//                .andExpect(jsonPath("$.content").value("질문에 대한 응답 내용"))
//                .andExpect(jsonPath("$.ageGroup").value("40대"))
//                .andExpect(jsonPath("$.averageHeight").value(175.0))
//                .andExpect(jsonPath("$.averageWeight").value(68.0))
//                .andExpect(jsonPath("$.smokingRate").value(30.0))
//                .andExpect(jsonPath("$.drinkingRate").value(40.0))
//                .andExpect(jsonPath("$.exerciseRate").value(50.0))
//                .andExpect(jsonPath("$.summaryEvaluation[0]").value("Evaluation 1"))
//                .andExpect(jsonPath("$.improvementSuggestions[0]").value("Suggestion 1"));
//    }
//
//    // 치매 검사 테스트
//    @Test
//    void testGetDementiaResponseWithNewRequest() throws Exception {
//        AlanDementiaRequest request = new AlanDementiaRequest();
//        request.setMemoryChange(MemoryChange.SOMETIMES);
//        request.setDailyConfusion(CommonFrequency.NONE);
//        request.setProblemSolvingChange(CommonFrequency.SOMETIMES);
//        request.setLanguageChange(CommonFrequency.NONE);
//        request.setKnowsDate(true);
//        request.setKnowsLocation(true);
//        request.setRemembersRecentEvents(true);
//        request.setFrequencyOfRepetition(CommonFrequency.NONE);
//        request.setLostItemsFrequency(CommonFrequency.SOMETIMES);
//        request.setDailyActivityDifficulty(CommonFrequency.NONE);
//        request.setGoingOutAlone(CommonFrequency.NONE);
//        request.setFinancialManagementDifficulty(CommonFrequency.NONE);
//        request.setAnxietyOrAggression(CommonFrequency.NONE);
//        request.setHallucinationOrDelusion(CommonFrequency.NONE);
//        request.setSleepPatternChange(CommonFrequency.NONE);
//        request.setHasChronicDiseases(false);
//        request.setHasStrokeHistory(false);
//        request.setHasFamilyDementia(false);
//
//        AlanDementiaResponse response = new AlanDementiaResponse();
//        response.setContent("정상 상태 평가");
//        response.setSummaryEvaluation(Arrays.asList("기억력 변화는 드뭅니다.", "인지 기능은 전반적으로 안정적입니다."));
//        response.setImprovementSuggestions(Arrays.asList("건강한 식단 유지", "적절한 신체 활동 지속"));
//        response.setAction(new AlanDementiaResponse.Action("normal_action", "상태가 양호합니다."));
//
//        // 서비스 모킹
//        when(alanService.fetchDementiaResponse(request)).thenReturn(response);
//
//        // MockMvc 테스트 수행
//        mockMvc.perform(post("/api/analysis/dementia")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").value("정상 상태 평가"))
//                .andExpect(jsonPath("$.summaryEvaluation[0]").value("기억력 변화는 드뭅니다."))
//                .andExpect(jsonPath("$.summaryEvaluation[1]").value("인지 기능은 전반적으로 안정적입니다."))
//                .andExpect(jsonPath("$.improvementSuggestions[0]").value("건강한 식단 유지"))
//                .andExpect(jsonPath("$.improvementSuggestions[1]").value("적절한 신체 활동 지속"))
//                .andExpect(jsonPath("$.action.name").value("normal_action"))
//                .andExpect(jsonPath("$.action.speak").value("상태가 양호합니다."));
//    }
//}