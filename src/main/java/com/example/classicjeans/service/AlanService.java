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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AlanService {

    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Autowired
    public AlanService(RestTemplateBuilder restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate.build();
        this.objectMapper = objectMapper;
    }

    public AlanBasicResponse fetchBasicResponse(String content) throws JsonProcessingException {
        String CLIENT_ID = "1a06fccc-d4f6-44ff-8daf-8dab60c82b93";
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", content)
                .queryParam("client_id", CLIENT_ID)
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println(response.getBody());

        return objectMapper.readValue(response.getBody(), AlanBasicResponse.class);
    }

    public AlanBasicResponse fetchHealthResponse(AlanHealthRequest request) throws JsonProcessingException {
        String CLIENT_ID = "1a06fccc-d4f6-44ff-8daf-8dab60c82b93";
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", request.toString())
                .queryParam("client_id", CLIENT_ID)
                .toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println(response.getBody());

        return objectMapper.readValue(response.getBody(), AlanBasicResponse.class);
    }

    // 치매 문진표 AI 검사
    public AlanDementiaResponse fetchDementiaResponse(AlanDementiaRequest request) throws JsonProcessingException {
        String CLIENT_ID = "CLIENT_ID 키 넣어야 함";
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", request.toString())
                .queryParam("client_id", CLIENT_ID)
                .toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println(response.getBody());

        return objectMapper.readValue(response.getBody(), AlanDementiaResponse.class);
    }

    // 종합 평가 추출 메서드
    private String extractSummaryEvaluation(String content) {
        Pattern pattern = Pattern.compile("### 종합 평가 \\(summaryEvaluation\\)[\\s\\S]*?\\n-.*?\\n(.*?)\\n### 개선 방법", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String summaryEvaluation = matcher.group(1).trim();
            return removeSourceLinks(summaryEvaluation);
        }
        return "종합 평가 정보가 없습니다.";
    }

    // 개선 방법 추출 메서드
    private String extractImprovementSuggestions(String content) {
        Pattern pattern = Pattern.compile("### 개선 방법 \\(improvementSuggestions\\)[\\s\\S]*?\\n(.*)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String suggestions = matcher.group(1).trim();
            return removeSourceLinks(suggestions);
        }
        return "개선 방법 정보가 없습니다.";
    }

    // 출처 링크 제거
    private String removeSourceLinks(String text) {
        String urlPattern = "\\[\\(출처\\d+\\)]\\(https?://[\\w./?&=-]+\\)";
        return text.replaceAll(urlPattern, "").trim();
    }
}
