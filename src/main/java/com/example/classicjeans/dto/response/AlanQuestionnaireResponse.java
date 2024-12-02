package com.example.classicjeans.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlanQuestionnaireResponse {

    @JsonProperty(value = "action")
    private AlanQuestionnaireResponse.Action action;

    @JsonProperty(value = "content")
    private String content;

    private String ageGroup;            // 연령대

    // 한국 평균 데이터 추가
    private Double averageHeight;       // 한국인 평균 키
    private Double averageWeight;       // 한국인 평균 체중
    private Double smokingRate;         // 한국인 평균 흡연율
    private Double drinkingRate;        // 한국인 평균 음주율
    private Double exerciseRate;        // 한국인 평균 운동 실천율

    // 종합 평가 내용
    @JsonProperty(value = "summaryEvaluation")
    private List<String> summaryEvaluation;

    // 개선 방법
    @JsonProperty(value = "improvementSuggestions")
    private List<String> improvementSuggestions;

    // Action 서브 클래스 정의
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Action {
        @JsonProperty(value = "name")
        private String name;

        @JsonProperty(value = "speak")
        private String speak;
    }
}
