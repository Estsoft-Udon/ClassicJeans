package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 복용 중인 약물
@Getter
@AllArgsConstructor
public enum Medication {
    NONE("없음", 0),
    BLOOD_PRESSURE_MEDICINE("고혈압 약물", 1),
    DIABETES_MEDICINE("당뇨병 약물", 2),
    HEART_MEDICINE("심장 약물", 3);

    private final String displayName;
    private final int impactScore;
}