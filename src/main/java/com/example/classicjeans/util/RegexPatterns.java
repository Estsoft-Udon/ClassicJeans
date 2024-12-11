package com.example.classicjeans.util;

public class RegexPatterns {
    public static final String SUMMARY_EVALUATION_PATTERN = "(?i)### 종합 평가 \\(summary\\s*evaluation\\):?\\s*\\n([\\s\\S]*?)\\n### 개선 방법";
    public static final String IMPROVEMENT_SUGGESTIONS_PATTERN = "(?i)### 개선 방법 \\(improvement\\s*suggestions\\)[\\s\\S]*?(\\d+\\.\\s.*?)(?=\\n$|$)";

    public static final String AGE_GROUP_PATTERN = "연령대:\\s*(\\d+대)";
    public static final String AVERAGE_HEIGHT_PATTERN = "\\((\\d+대) (남성|여성) 평균: 약 (\\d+(\\.\\d+)?)\\s*cm";
    public static final String AVERAGE_WEIGHT_PATTERN = "\\((\\d+대) (남성|여성) 평균: 약 (\\d+(\\.\\d+)?)\\s*kg";
    public static final String SMOKING_RATE_PATTERN = "한국인 (남성|여성) 흡연율: 약 (\\d+(\\.\\d+)?)\\s*%";
    public static final String DRINKING_RATE_PATTERN = "한국인 (남성|여성) 음주율: 약 (\\d+(\\.\\d+)?)\\s*%";
    public static final String EXERCISE_RATE_PATTERN = "한국인 (남성|여성) 걷기 실천율: 약 (\\d+(\\.\\d+)?)\\s*%";

    public static final String URL_PATTERN = "\\[\\(출처\\d+\\)]\\(https?://[\\w./?&=-]+\\)";
}