package com.example.classicjeans.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
public class AlanResponseDto {
    @JsonProperty("id")
    private String id;  // id 필드 추가

    @JsonProperty("event")
    private String event;  // event 필드 추가

    @JsonProperty("type")
    private String type;  // 응답 타입

    @JsonProperty("data")
    private Map<String, Object> data;  // 데이터 내용 (key-value 형태)
}
