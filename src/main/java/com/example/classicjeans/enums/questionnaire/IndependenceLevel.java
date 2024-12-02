package com.example.classicjeans.enums.questionnaire;

// 독립성 수준
public enum IndependenceLevel {
    FULLY_INDEPENDENT("완전히 독립적"),
    PARTIALLY_DEPENDENT("부분적으로 의존적"),
    FULLY_DEPENDENT("완전히 의존적");

    private final String displayName;

    IndependenceLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
