package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 음주 빈도
@Getter
@AllArgsConstructor
public enum AlcoholConsumption {
    NONE("없음", 0),
    OCCASIONAL("가끔", 1),
    REGULAR("정기적", 4),
    FREQUENT("자주", 6);

    private final String displayName;
    private final int impactScore;
}