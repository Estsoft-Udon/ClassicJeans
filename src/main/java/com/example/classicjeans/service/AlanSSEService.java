package com.example.classicjeans.service;

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
    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question/sse-streaming";

    private final RestTemplate restTemplate;

    public SseEmitter getChatResponse(String content) {
        // SSE 응답을 위한 SseEmitter 생성
        SseEmitter emitter = new SseEmitter();

        // 요청 데이터 생성
        String uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("content", content)
                .queryParam("client_id", CLIENT_ID)
                .toUriString();

        // 비동기적으로 SSE 스트리밍을 처리
        new Thread(() -> {
            try {
                // 외부 API 호출 (기존 `getChatResponse` 방식으로)
                String response = restTemplate.getForObject(uri, String.class);

                // 외부 응답 데이터를 스트리밍 (SSE 방식으로 클라이언트로 전송)
                emitter.send(SseEmitter.event()
                        .data(response)
                        .name("message"));

                // 스트리밍 종료
                emitter.complete();
            } catch (Exception e) {
                // 오류 발생 시 에러 메시지 전송
                try {
                    emitter.send(SseEmitter.event()
                            .data("Error occurred: " + e.getMessage())
                            .name("error"));
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
            }
        }).start();

        return emitter;
    }
}
