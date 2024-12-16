package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.example.classicjeans.dto.response.AlanQuestionnaireResponse;
import com.example.classicjeans.entity.*;
import com.example.classicjeans.repository.DementiaDataRepository;
import com.example.classicjeans.repository.QuestionnaireDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.classicjeans.util.SecurityUtil.*;

import static com.example.classicjeans.util.RegexPatterns.*;

@Service
public class AlanService {

    @Value("${CLIENT_ID_3}")
    private String CLIENT_ID_3;
    @Value("${CLIENT_ID_4}")
    private String CLIENT_ID_4;

    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";
    private static final String DELETE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/reset-state";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final QuestionnaireDataRepository questionnaireDataRepository;
    private final DementiaDataRepository dementiaDataRepository;

    @Autowired
    public AlanService(RestTemplateBuilder restTemplate, ObjectMapper objectMapper,
                       QuestionnaireDataRepository questionnaireDataRepository,
                       DementiaDataRepository dementiaDataRepository) {
        this.restTemplate = restTemplate.build();
        this.objectMapper = objectMapper;
        this.questionnaireDataRepository = questionnaireDataRepository;
        this.dementiaDataRepository = dementiaDataRepository;
    }

    // 기본 문진표 AI 검사
    public AlanQuestionnaireResponse fetchQuestionnaireResponse(AlanQuestionnaireRequest request)
            throws JsonProcessingException {
        //resetPreviousData();
        String responseBody = fetchResponse(request.toString());
        AlanQuestionnaireResponse response = parseQuestionnaireResponse(responseBody);
        response.setHealthIndex(calculateHealthIndex(request));
        saveQuestionnaireData(request, response);
        return response;
    }

    // 기본 검사 결과 저장
    public void saveQuestionnaireData(AlanQuestionnaireRequest request, AlanQuestionnaireResponse response) {
        boolean isFamilyInfo = request.getFamily() != null;
        QuestionnaireData data = new QuestionnaireData(
                request.getUser(),
                request.getFamily(),
                isFamilyInfo ? request.getFamily().getAge() : getLoggedInUser().getAge(),
                isFamilyInfo ? request.getFamily().getGender() : getLoggedInUser().getGender(),
                request.getHeight(),
                request.getWeight(),
                request.getChronicDisease(),
                request.getHospitalVisit(),
                request.getCurrentMedication(),
                request.getSmokingStatus(),
                request.getAlcoholConsumption(),
                request.getExerciseFrequency(),
                request.getDietPattern(),
                request.getMoodStatus(),
                request.getSleepPattern(),
                request.getIndependenceLevel(),
                request.getSocialParticipation(),
                request.isHasGeneticDisease(),
                request.getWeightChange(),
                request.isHasAllergy(),
                response.getAgeGroup(),
                response.getAverageHeight(),
                response.getAverageWeight(),
                response.getSmokingRate(),
                response.getDrinkingRate(),
                response.getExerciseRate(),
                response.getSummaryEvaluation(),
                response.getImprovementSuggestions(),
                response.getHealthIndex()
        );
        for (SummaryEvaluation evaluation : response.getSummaryEvaluation()) {
            evaluation.setQuestionnaireData(data);
        }
        for (ImprovementSuggestions suggestion : response.getImprovementSuggestions()) {
            suggestion.setQuestionnaireData(data);
        }
        questionnaireDataRepository.save(data);
    }

    // 치매 문진표 AI 검사
    public AlanDementiaResponse fetchDementiaResponse(AlanDementiaRequest request) throws JsonProcessingException {
//        resetPreviousData();
        String responseBody = fetchResponse(request.toString());
        AlanDementiaResponse response = parseAIResponse(responseBody);
        saveDementiaData(request, response);
        return response;
    }

    // 치매 검진 결과 저장
    private void saveDementiaData(AlanDementiaRequest request, AlanDementiaResponse response) {
        DementiaData data = new DementiaData(
                request.getUser(),
                request.getFamily(),
                request.getMemoryChange(),
                request.getDailyConfusion(),
                request.getProblemSolvingChange(),
                request.getLanguageChange(),
                request.isKnowsDate(),
                request.isKnowsLocation(),
                request.isRemembersRecentEvents(),
                request.getFrequencyOfRepetition(),
                request.getLostItemsFrequency(),
                request.getDailyActivityDifficulty(),
                request.getGoingOutAlone(),
                request.getFinancialManagementDifficulty(),
                request.getAnxietyOrAggression(),
                request.getHallucinationOrDelusion(),
                request.getSleepPatternChange(),
                request.isHasChronicDiseases(),
                request.isHasStrokeHistory(),
                request.isHasFamilyDementia(),
                response.getSummaryEvaluation(),
                response.getImprovementSuggestions()
        );
        for (SummaryEvaluation evaluation : response.getSummaryEvaluation()) {
            evaluation.setDementiaData(data);
        }
        for (ImprovementSuggestions suggestion : response.getImprovementSuggestions()) {
            suggestion.setDementiaData(data);
        }
        dementiaDataRepository.save(data);
    }

    // URI 생성과 요청 전송
    private String fetchResponse(String content) {
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", content)
                .queryParam("client_id", CLIENT_ID_3)
                .toUriString();
        return restTemplate.getForEntity(uri, String.class).getBody();
    }

    // 메소드 실행 전에 이전 데이터를 초기화
    private void resetPreviousData() {
        String uri = UriComponentsBuilder
                .fromHttpUrl(DELETE_URL)
                .toUriString();

        String jsonBody = "{\"client_id\":\"" + CLIENT_ID_3 + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        } catch (HttpStatusCodeException e) {
            System.err.println("Error during reset: " + e.getResponseBodyAsString());
        }
    }

    // AlanQuestionnaireResponse 파싱
    private AlanQuestionnaireResponse parseQuestionnaireResponse(String aiResponseContent)
            throws JsonProcessingException {
        AlanQuestionnaireResponse response = new AlanQuestionnaireResponse();
        JsonNode rootNode = objectMapper.readTree(aiResponseContent);

        JsonNode actionNode = rootNode.path("action");
        AlanQuestionnaireResponse.Action action = new AlanQuestionnaireResponse.Action(
                actionNode.path("name").asText(),
                actionNode.path("speak").asText()
        );
        response.setAction(action);

        response.setContent(rootNode.path("content").asText());

        parseHealthData(response);
        response.setSummaryEvaluation(
                extractContent(response.getContent(), SUMMARY_EVALUATION_PATTERN, SummaryEvaluation.class)
        );
        response.setImprovementSuggestions(
                extractContent(response.getContent(), IMPROVEMENT_SUGGESTIONS_PATTERN, ImprovementSuggestions.class)
        );

        return response;
    }

    // AlanDementiaResponse 파싱
    private AlanDementiaResponse parseAIResponse(String aiResponseContent) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(aiResponseContent);

        JsonNode actionNode = rootNode.path("action");
        AlanDementiaResponse.Action action = new AlanDementiaResponse.Action(
                actionNode.path("name").asText(),
                actionNode.path("speak").asText()
        );

        String content = rootNode.path("content").asText();
        List<SummaryEvaluation> summaryEvaluation = extractContent(content, SUMMARY_EVALUATION_PATTERN,
                SummaryEvaluation.class);
        List<ImprovementSuggestions> improvementSuggestions = extractContent(content, IMPROVEMENT_SUGGESTIONS_PATTERN,
                ImprovementSuggestions.class);

        return new AlanDementiaResponse(action, content, summaryEvaluation, improvementSuggestions);
    }

    // 한국 평균 데이터 파싱
    private void parseHealthData(AlanQuestionnaireResponse response) {
        String content = response.getContent();

        response.setAverageHeight(extractDouble(content, AVERAGE_HEIGHT_PATTERN));
        response.setAverageWeight(extractDouble(content, AVERAGE_WEIGHT_PATTERN));
        response.setSmokingRate(extractDouble(content, SMOKING_RATE_PATTERN));
        response.setDrinkingRate(extractDouble(content, DRINKING_RATE_PATTERN));
        response.setExerciseRate(extractDouble(content, EXERCISE_RATE_PATTERN));

        String ageGroup = extractAgeGroup(content);
        if (ageGroup != null) {
            response.setAgeGroup(ageGroup);
        }
    }

    // 연령대 추출 함수
    private String extractAgeGroup(String content) {
        Pattern pattern = Pattern.compile(AGE_GROUP_PATTERN);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // 정규 표현식으로 숫자 추출
    private Double extractDouble(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            if (regex.contains("흡연율") || regex.contains("음주율") || regex.contains("운동율")) {
                return Double.parseDouble(matcher.group(2));
            } else if (regex.contains("실천율")) {
                return Double.parseDouble(matcher.group(3));
            } else {
                return Double.parseDouble(matcher.group(3));
            }
        }
        return null;
    }

    // 종합 평가, 개선 방법 추출 메서드
    private <T> List<T> extractContent(String content, String patternString, Class<T> targetClass) {
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        List<T> results = new ArrayList<>();

        while (matcher.find()) {
            String matchedContent = matcher.group(1).trim();
            if (!matchedContent.isEmpty()) {
                String[] items = matchedContent.split("\n");
                for (String item : items) {
                    String cleanedItem = removeSourceLinks(item.trim());
                    if (cleanedItem.isEmpty() || cleanedItem.startsWith(":") || cleanedItem.startsWith("이 정보를 바탕으로")
                            || cleanedItem.startsWith("### 참고 자료") || cleanedItem.startsWith("- **")) {
                        continue;
                    }
                    try {
                        T entity = createEntity(cleanedItem, targetClass);
                        results.add(entity);
                    } catch (Exception e) {
                        System.err.println("Failed to create entity: " + e.getMessage());
                    }
                }
            }
        }
        if (results.isEmpty()) {
            try {
                T emptyEntity = createEntity("정보가 없습니다.", targetClass);
                results.add(emptyEntity);
            } catch (Exception e) {
                System.err.println("Failed to create empty entity: " + e.getMessage());
            }
        }
        return results;
    }

    // 문자열 데이터를 받아 지정된 클래스 타입의 엔티티 객체를 생성
    private <T> T createEntity(String content, Class<T> targetClass) throws Exception {
        if (targetClass == SummaryEvaluation.class) {
            SummaryEvaluation evaluation = new SummaryEvaluation();
            evaluation.setEvaluation(content);
            return targetClass.cast(evaluation);
        } else if (targetClass == ImprovementSuggestions.class) {
            ImprovementSuggestions suggestion = new ImprovementSuggestions();
            suggestion.setSuggestion(content);
            return targetClass.cast(suggestion);
        }
        throw new IllegalArgumentException("Unsupported target class: " + targetClass.getName());
    }

    // 출처 링크 제거
    private String removeSourceLinks(String text) {
        return text.replaceAll(URL_PATTERN, "").trim();
    }

    // 건강 지수 계산
    public Double calculateHealthIndex(AlanQuestionnaireRequest request) {
        double score = 66.5;
        double bmi = calculateBMI(request.getHeight(), request.getWeight());
        if (bmi < 18.5) {
            score -= 7.5;
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            score += 3.5;
        } else if (bmi >= 25 && bmi <= 29.9) {
            score -= 4.5;
        } else {
            score -= 8.5;
        }

        // 만성 질환 상태
        if (request.getChronicDisease() != null) {
            score -= request.getChronicDisease().getImpactScore() * 2.5;
        }

        // 병원 방문 여부
        if (request.getHospitalVisit() != null) {
            score += request.getHospitalVisit().getImpactScore();
        }

        // 약물 영향
        if (request.getCurrentMedication() != null) {
            score -= request.getCurrentMedication().getImpactScore() * 0.8;
        }

        // 흡연 여부
        if (request.getSmokingStatus() != null) {
            score += request.getSmokingStatus().getImpactScore();
        }

        // 음주 빈도
        if (request.getAlcoholConsumption() != null) {
            score += request.getAlcoholConsumption().getImpactScore();
        }

        // 운동 빈도
        if (request.getExerciseFrequency() != null) {
            score += request.getExerciseFrequency().getImpactScore();
        }

        // 식습관
        if (request.getDietPattern() != null) {
            score += request.getDietPattern().getImpactScore();
        }

        // 정신 건강
        if (request.getMoodStatus() != null) {
            score += request.getMoodStatus().getImpactScore();
        }
        if (request.getSleepPattern() != null) {
            score += request.getSleepPattern().getImpactScore();
        }

        // 사회적 활동
        if (request.getIndependenceLevel() != null) {
            score += request.getIndependenceLevel().getImpactScore();
        }
        if (request.getSocialParticipation() != null) {
            score += request.getSocialParticipation().getImpactScore();
        }

        // 가족력
        if (request.isHasGeneticDisease()) {
            score -= 7.5;
        }

        // 체중 변화
        if (request.getWeightChange() != null) {
            score += request.getWeightChange().getImpactScore();
        }

        // 알레르기 여부
        if (request.isHasAllergy()) {
            score -= 3.5;
        }

        // 점수 제한 (최소 0, 최대 100)
        return Math.round(Math.max(0, Math.min(100, score)) * 100.0) / 100.0;
    }

    // BMI 계산
    public double calculateBMI(double height, double weight) {
        return weight / Math.pow(height / 100, 2);
    }
}