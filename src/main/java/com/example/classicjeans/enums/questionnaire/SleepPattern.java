package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 수면 패턴
@Getter
@AllArgsConstructor
public enum SleepPattern {
    SUFFICIENT("충분함", 3),
    INSUFFICIENT("부족함", -7),
    IRREGULAR("불규칙", -4);

    private final String displayName;
    private final int impactScore;
}