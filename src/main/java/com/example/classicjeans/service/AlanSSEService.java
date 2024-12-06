package com.example.classicjeans.service;


import static com.example.classicjeans.util.MarkdownRenderer.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AlanSSEService {

    @Value("${openai.api.key}")
    private String CLIENT_ID;
    private static final String SSE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question/sse-streaming";
    private static final String DELETE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/reset-state";

    private final ObjectMapper objectMapper;

    public void streamChatResponse(String content, SseEmitter emitter) throws IOException {
        // 요청 데이터 생성
        String uri = UriComponentsBuilder
                .fromUriString(SSE_URL)
                .queryParam("content", content)
                .queryParam("client_id", CLIENT_ID)
                .toUriString();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder fullContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data: ")) {

                    // "data: " 이후의 JSON 데이터 추출
                    String json = line.substring(6).trim();

                    // "complete"가 포함되면 스트리밍 종료 이벤트 전송
                    if (json.contains("complete")) {
                        emitter.send(SseEmitter.event().name("completed"));
                        System.out.println("메세지가 종료됩니다.");
                        break;
                    }

                    try {
                        // JSON 파싱
                        JsonNode extractedContent = objectMapper.readTree(json)
                                .path("data")
                                .path("content");

                        String contentText = extractedContent.asText();

                        fullContent.append(contentText);

                        // 마크다운을 HTML로 변환
                        String htmlContent = convertMarkdownToHtml(fullContent.toString());

                        emitter.send(SseEmitter.event()
                                .name("message")
                                .data(htmlContent));

                    } catch (JsonParseException e) {
                        handleJsonParsingError(e);
                    }
                }
            }
            emitter.complete();
        } catch (Exception e) {
            handleError(e, connection);
            emitter.completeWithError(e);
            throw e;
        }
    }


    // delete api 사용하는 방법
    public String resetChat(String content) {
        // 요청 데이터 생성
        String uri = UriComponentsBuilder
                .fromUriString(DELETE_URL)
                .toUriString();

        return null;
    }

    private void handleJsonParsingError(Exception e) {
        // JSON 파싱 예외 처리
        System.err.println("JSON 파싱 에러: " + e.getMessage());
    }

    private void handleError(Exception e, HttpURLConnection connection) {
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            String errorResponse = errorReader.lines()
                    .reduce("", (acc, line) -> acc + line + "\n");
            System.err.println("Error Response: " + errorResponse);
        } catch (IOException ioException) {
            System.err.println("Error reading error response: " + ioException.getMessage());
        }
        System.err.println("스트리밍 처리 중 에러: " + e.getMessage());
    }
}
