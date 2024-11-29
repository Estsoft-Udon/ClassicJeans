package com.example.classicjeans.enums.questionnaire;

// 복용 중인 약물
public enum Medication {
    NONE("없음"),
    BLOOD_PRESSURE_MEDICINE("고혈압 약물"),
    DIABETES_MEDICINE("당뇨병 약물"),
    HEART_MEDICINE("심장 약물");

    private final String displayName;

    Medication(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}