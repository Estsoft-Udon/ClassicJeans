package com.example.classicjeans.enums.questionnaire;

// 만성 질환 상태
public enum ChronicDisease {
    NONE("없음"),
    DIABETES("당뇨병"),
    HYPERTENSION("고혈압"),
    HEART_DISEASE("심장 질환");

    private final String displayName;

    ChronicDisease(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}