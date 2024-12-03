package com.example.classicjeans.enums.questionnaire;

// 음주 빈도
public enum AlcoholConsumption {
    NONE("없음"),
    OCCASIONAL("가끔"),
    REGULAR("정기적"),
    FREQUENT("자주");

    private final String displayName;

    AlcoholConsumption(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
