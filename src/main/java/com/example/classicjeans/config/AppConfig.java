package com.example.classicjeans.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // SSE-Streaming Json parse 추가
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
}
