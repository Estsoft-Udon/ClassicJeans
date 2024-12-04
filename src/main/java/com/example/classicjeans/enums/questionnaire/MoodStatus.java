package com.example.classicjeans.enums.questionnaire;

// 최근 2주간 기분 상태
public enum MoodStatus {
    GOOD("좋음"),
    OCCASIONALLY_ANXIOUS_OR_DEPRESSED("가끔 불안하거나 우울함"),
    PERSISTENTLY_ANXIOUS_OR_DEPRESSED("지속적인 불안 또는 우울");

    private final String displayName;

    MoodStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}