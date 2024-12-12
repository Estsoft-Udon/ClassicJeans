package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 만성 질환 상태
@Getter
@AllArgsConstructor
public enum ChronicDisease {
    NONE("없음", 0),
    DIABETES("당뇨병", 2),
    HYPERTENSION("고혈압", 1),
    HEART_DISEASE("심장 질환", 3);

    private final String displayName;
    private final int impactScore;
}