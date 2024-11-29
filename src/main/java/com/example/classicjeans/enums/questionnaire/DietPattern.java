package com.example.classicjeans.enums.questionnaire;

// 식사 패턴
public enum DietPattern {
    REGULAR_3_MEALS("정기적인 3끼 식사"),
    IRREGULAR_MEALS("불규칙적인 식사");

    private final String displayName;

    DietPattern(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}