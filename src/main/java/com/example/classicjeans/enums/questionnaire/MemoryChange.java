package com.example.classicjeans.enums.questionnaire;

// 기억력 변화
public enum MemoryChange {
    NO_CHANGE("변화 없음"),
    SOMETIMES("가끔"),
    OFTEN("자주");

    private final String displayName;

    MemoryChange(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}