package com.example.classicjeans.enums.questionnaire;

// 운동 빈도
public enum ExerciseFrequency {
    NONE("없음"),
    OCCASIONAL("가끔"),
    REGULAR("정기적");

    private final String displayName;

    ExerciseFrequency(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}