package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 흡연 상태
@Getter
@AllArgsConstructor
public enum SmokingStatus {
    CURRENTLY_SMOKING("현재 흡연 중", 5),
    FORMER_SMOKER("과거에 흡연했으나 현재는 금연", 2),
    NON_SMOKER("비흡연자", 0);

    private final String displayName;
    private final int impactScore;
}