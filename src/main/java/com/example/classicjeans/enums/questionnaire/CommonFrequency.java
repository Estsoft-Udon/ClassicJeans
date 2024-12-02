package com.example.classicjeans.enums.questionnaire;

// 빈도와 정도를 나타내는 공통 Enum
public enum CommonFrequency {
    NONE("없음"),
    SOMETIMES("가끔"),
    OFTEN("자주");

    private final String displayName;

    CommonFrequency(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}