package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 식사 패턴
@Getter
@AllArgsConstructor
public enum DietPattern {
    REGULAR_3_MEALS("정기적인 3끼 식사", 5),
    IRREGULAR_MEALS("불규칙적인 식사", -5);

    private final String displayName;
    private final int impactScore;
}