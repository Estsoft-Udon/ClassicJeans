package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 최근 2주간 기분 상태
@Getter
@AllArgsConstructor
public enum MoodStatus {
    GOOD("좋음", 5),
    OCCASIONALLY_ANXIOUS_OR_DEPRESSED("가끔 불안하거나 우울함", -3),
    PERSISTENTLY_ANXIOUS_OR_DEPRESSED("지속적인 불안 또는 우울", -7);

    private final String displayName;
    private final int impactScore;
}