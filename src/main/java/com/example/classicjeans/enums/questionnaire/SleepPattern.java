package com.example.classicjeans.enums.questionnaire;

// 수면 패턴
public enum SleepPattern {
    SUFFICIENT("충분함"),
    INSUFFICIENT("부족함"),
    IRREGULAR("불규칙");

    private final String displayName;

    SleepPattern(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}