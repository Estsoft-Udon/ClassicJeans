package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanHealthRequest;
import com.example.classicjeans.dto.response.AlanBasicResponse;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.dto.response.AlanDementiaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    }

    // 오늘의 운세
    public AlanBaziResponse fetchBazi(AlanBaziRequest request) throws JsonProcessingException {
        String CLIENT_ID = "c4bbb624-af0f-4304-9557-740cb16dc30a";
        // URI 생성
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", request)  // AlanBaziRequest.toString() 호출
                .queryParam("client_id", CLIENT_ID)
                .toUriString();

        // API 호출
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String responseBody = response.getBody();

        // JSON 파싱
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode contentNode = rootNode.path("content");

        if (contentNode.isTextual()) {
            String cleanedContent = removeBaziContent(contentNode.asText()); // 정리된 텍스트 처리

            // JSON 수정
            ((ObjectNode) rootNode).put("content", cleanedContent);
        }

        // System.out.println("Cleaned content: " + rootNode.toString());  // 처리된 텍스트 출력

        // 수정된 JSON을 AlanBaziResponse 객체로 변환하여 반환
        return objectMapper.treeToValue(rootNode, AlanBaziResponse.class);

    }
    // 운세 결과 텍스트 정리
    private String removeBaziContent(String text) {
        return text
                .replaceAll(URL_PATTERN, "")
                .replaceAll("\\d{4}년 \\d{1,2}월 \\d{1,2}일에 태어난 (남성|여성)의\\s*", "") // 0000년 00월 00일에 태어난 남성/여성" 제거
                .replaceAll("\\[.*?]\\(.*?\\)", "")
                .replaceAll("\\d{4}년생은\\s*", "")
                .replaceAll("이 외에도.*", "")
                .replaceAll("다른 띠의 내용은 포함하지 않았습니다\\.?", "")
                .replaceAll("\\*\\*\\*\\*:\\s*-\\s*", "")
                .replaceAll(":\\s*:\\s*:", "") // ': : :' 제거
                .trim();
    }
}