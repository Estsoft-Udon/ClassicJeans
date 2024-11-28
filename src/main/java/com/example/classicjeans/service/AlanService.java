package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanHealthRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.dto.response.AlanBasicResponse;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.example.classicjeans.dto.response.AlanQuestionnaireResponse;
import com.example.classicjeans.util.RegexPatterns;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class AlanService {

    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";
    private static final String DELETE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/reset-state";
    private static final String CLIENT_ID = "CLIENT_ID 키 넣어야 함";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AlanService(RestTemplateBuilder restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate.build();
        this.objectMapper = objectMapper;
    }

    // 앨런AI test - 추후 제거
    public AlanBasicResponse fetchBasicResponse(String content) throws JsonProcessingException {
        String responseBody = fetchResponse(content);
        return objectMapper.readValue(responseBody, AlanBasicResponse.class);
    }

    // 앨런AI test2 - 추후 제거
    public AlanBasicResponse fetchHealthResponse(AlanHealthRequest request) throws JsonProcessingException {
        String responseBody = fetchResponse(request.toString());
        return objectMapper.readValue(responseBody, AlanBasicResponse.class);
    }

    // 기본 문진표 AI 검사
    public AlanQuestionnaireResponse fetchQuestionnaireResponse(AlanQuestionnaireRequest request) throws JsonProcessingException {
//        resetPreviousData();
        String responseBody = fetchResponse(request.toString());
        return parseQuestionnaireResponse(responseBody);
    }

    // 치매 문진표 AI 검사
    public AlanDementiaResponse fetchDementiaResponse(AlanDementiaRequest request) throws JsonProcessingException {
//        resetPreviousData();
        String responseBody = fetchResponse(request.toString());
        return parseAIResponse(responseBody);
    }

    // URI 생성과 요청 전송
    private String fetchResponse(String content) {
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", content)
                .queryParam("client_id", CLIENT_ID)
                .toUriString();
        return restTemplate.getForEntity(uri, String.class).getBody();
    }

    // 메소드 실행 전에 이전 데이터를 초기화
    private void resetPreviousData() {
        String uri = UriComponentsBuilder
                .fromHttpUrl(DELETE_URL)
                .toUriString();

        String jsonBody = "{\"client_id\":\"" + CLIENT_ID + "\"}";

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
    private AlanQuestionnaireResponse parseQuestionnaireResponse(String aiResponseContent) throws JsonProcessingException {
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
        response.setSummaryEvaluation(extractContent(response.getContent(), RegexPatterns.SUMMARY_EVALUATION_PATTERN));
        response.setImprovementSuggestions(extractContent(response.getContent(), RegexPatterns.IMPROVEMENT_SUGGESTIONS_PATTERN));

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
        List<String> summaryEvaluation = extractContent(content, RegexPatterns.SUMMARY_EVALUATION_PATTERN);
        List<String> improvementSuggestions = extractContent(content, RegexPatterns.IMPROVEMENT_SUGGESTIONS_PATTERN);

        return new AlanDementiaResponse(action, content, summaryEvaluation, improvementSuggestions);
    }

    // 사용자 건강 데이터 및 한국 평균 데이터 파싱
    private void parseHealthData(AlanQuestionnaireResponse response) {
        String content = response.getContent();

        response.setUserHeight(extractDouble(content, RegexPatterns.HEIGHT_PATTERN));
        response.setAverageHeight(extractDouble(content, RegexPatterns.AVERAGE_HEIGHT_PATTERN));
        response.setUserWeight(extractDouble(content, RegexPatterns.WEIGHT_PATTERN));
        response.setAverageWeight(extractDouble(content, RegexPatterns.AVERAGE_WEIGHT_PATTERN));

        response.setSmokingRate(extractDouble(content, RegexPatterns.SMOKING_RATE_PATTERN));
        response.setDrinkingRate(extractDouble(content, RegexPatterns.DRINKING_RATE_PATTERN));
        response.setExerciseRate(extractDouble(content, RegexPatterns.EXERCISE_RATE_PATTERN));
    }

    // 정규 표현식으로 숫자 추출
    private Double extractDouble(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return null;
    }

    // 종합 평가, 개선 방법 추출 메서드
    private List<String> extractContent(String content, String patternString) {
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        List<String> results = new ArrayList<>();

        while (matcher.find()) {
            String matchedContent = matcher.group(1).trim();
            String[] items = matchedContent.split("\n");
            for (String item : items) {
                if (item.startsWith("-")) {
                    results.add(removeSourceLinks(item.trim()));
                } else if (item.matches("^\\d+\\.\\s.*")) {
                    results.add(removeSourceLinks(item.trim()));
                }
            }
        }
        if (results.isEmpty()) {
            results.add("정보가 없습니다.");
        }

        return results;
    }

    // 출처 링크 제거
    private String removeSourceLinks(String text) {
        return text.replaceAll(RegexPatterns.URL_PATTERN, "").trim();
    }
}