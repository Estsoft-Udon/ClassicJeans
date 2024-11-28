package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanHealthRequest;
import com.example.classicjeans.dto.response.AlanBasicResponse;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AlanService {

    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";
    private static final String CLIENT_ID = "CLIENT_ID 키 넣어야 함";

    private static final String SUMMARY_EVALUATION_PATTERN = "### 종합 평가 \\(summaryEvaluation\\)[\\s\\S]*?\\n-.*?\\n(.*?)\\n### 개선 방법";
    private static final String IMPROVEMENT_SUGGESTIONS_PATTERN = "### 개선 방법 \\(improvementSuggestions\\)[\\s\\S]*?\\n(.*)";
    private static final String URL_PATTERN = "\\[\\(출처\\d+\\)]\\(https?://[\\w./?&=-]+\\)";

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

    // 치매 문진표 AI 검사
    public AlanDementiaResponse fetchDementiaResponse(AlanDementiaRequest request) throws JsonProcessingException {
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

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }

    // AI 응답을 AlanDementiaResponse로 변환
    private AlanDementiaResponse parseAIResponse(String aiResponseContent) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(aiResponseContent);

        JsonNode actionNode = rootNode.path("action");
        AlanDementiaResponse.Action action = new AlanDementiaResponse.Action(
                actionNode.path("name").asText(),
                actionNode.path("speak").asText()
        );

        String content = rootNode.path("content").asText();
        String summaryEvaluation = extractContent(content, SUMMARY_EVALUATION_PATTERN);
        String improvementSuggestions = extractContent(content, IMPROVEMENT_SUGGESTIONS_PATTERN);

        return new AlanDementiaResponse(action, content, summaryEvaluation, improvementSuggestions);
    }

    // 종합 평가, 개선 방법 추출 메서드
    private String extractContent(String content, String patternString) {
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return removeSourceLinks(matcher.group(1).trim());
        }
        return "정보가 없습니다.";
    }

    // 출처 링크 제거
    private String removeSourceLinks(String text) {
        return text.replaceAll(URL_PATTERN, "").trim();
//        return objectMapper.readValue(response.getBody(), AlanBasicResponse.class);
    }

    // 오늘의 운세
    public AlanBasicResponse fetchBazi() throws JsonProcessingException {
        String CLIENT_ID = "1a06fccc-d4f6-44ff-8daf-8dab60c82b93";

        //request -> response
        String s = "1999년 11월 13일 오늘의 운세 알려줘";
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", s)
                .queryParam("client_id", CLIENT_ID)
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println(response.getBody());

        return objectMapper.readValue(response.getBody(), AlanBasicResponse.class);
    }
}