package com.example.classicjeans.enums.questionnaire;

// 최근 체중 변화
public enum WeightChange {
    NONE("변화 없음"),
    WEIGHT_GAIN("체중 증가"),
    WEIGHT_LOSS("체중 감소");

    private final String displayName;

    WeightChange(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}