package com.example.classicjeans.dto.response;

import com.example.classicjeans.entity.ImprovementSuggestions;
import com.example.classicjeans.entity.SummaryEvaluation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlanDementiaResponse {

    @JsonProperty(value = "action")
    private Action action;  // action 객체로 AI 응답의 액션 부분을 처리

    @JsonProperty(value = "content")
    private String content; // AI 응답의 "content" 필드에 있는 내용

    // 종합 평가 내용
    @JsonProperty(value = "summaryEvaluation")
    private List<SummaryEvaluation> summaryEvaluation;  // 종합평가 내용 추가

    // 개선 방법
    @JsonProperty(value = "improvementSuggestions")
    private List<ImprovementSuggestions> improvementSuggestions; // 개선 방법 추가

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
