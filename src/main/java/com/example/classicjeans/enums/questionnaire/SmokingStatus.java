package com.example.classicjeans.enums.questionnaire;

// 흡연 상태
public enum SmokingStatus {
    CURRENTLY_SMOKING("현재 흡연 중"),
    FORMER_SMOKER("전 흡연자"),
    NON_SMOKER("비흡연자");

    private final String displayName;

    SmokingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}