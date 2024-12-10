package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 독립성 수준
@Getter
@AllArgsConstructor
public enum IndependenceLevel {
    FULLY_INDEPENDENT("완전히 독립적", 5),
    PARTIALLY_DEPENDENT("부분적으로 의존적", -3),
    FULLY_DEPENDENT("완전히 의존적", -7);

    private final String displayName;
    private final int impactScore;
}