package com.example.classicjeans.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";

    private final RestTemplate restTemplate;

    public String getChatResponse(String content) {
        // 요청 데이터 생성
        String uri = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("content", content)
                .queryParam("client_id", CLIENT_ID)
                .toUriString();

        return restTemplate.getForObject(uri, String.class);
    }
}
