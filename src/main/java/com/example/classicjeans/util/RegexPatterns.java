package com.example.classicjeans.util;

public class RegexPatterns {
    public static final String SUMMARY_EVALUATION_PATTERN = "### 종합 평가 \\(Summary Evaluation\\)[\\s\\S]*?\\n-.*?\\n(.*?)\\n### 개선 방법";
    public static final String IMPROVEMENT_SUGGESTIONS_PATTERN = "### 개선 방법 \\(Improvement Suggestions\\)[\\s\\S]*?(\\d+\\.\\s.*?)(?=\\n$|$)";

    public static final String HEIGHT_PATTERN = "\\*\\*키\\*\\*: (\\d+(\\.\\d+)?)cm";
    public static final String AVERAGE_HEIGHT_PATTERN = "한국인 남성 평균: 약 (\\d+(\\.\\d+)?)cm";
    public static final String WEIGHT_PATTERN = "\\*\\*체중\\*\\*: (\\d+(\\.\\d+)?)kg";
    public static final String AVERAGE_WEIGHT_PATTERN = "한국인 남성 평균: 약 (\\d+(\\.\\d+)?)kg";
    public static final String SMOKING_RATE_PATTERN = "한국인 흡연율: 약 (\\d+(\\.\\d+)?)%";
    public static final String DRINKING_RATE_PATTERN = "한국인 음주율: 약 (\\d+(\\.\\d+)?)%";
    public static final String EXERCISE_RATE_PATTERN = "한국인 운동 실천율: 약 (\\d+(\\.\\d+)?)%";

    public static final String URL_PATTERN = "\\[\\(출처\\d+\\)]\\(https?://[\\w./?&=-]+\\)";

    public static final String AGE_GROUP_PATTERN = "연령대:\\s*(\\d+대)";
}