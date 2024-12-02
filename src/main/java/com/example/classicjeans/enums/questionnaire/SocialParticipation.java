package com.example.classicjeans.enums.questionnaire;

// 사회적 활동 참여 빈도
public enum SocialParticipation {
    NONE("없음"),
    OCCASIONAL("가끔"),
    FREQUENT("자주");

    private final String displayName;

    SocialParticipation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}