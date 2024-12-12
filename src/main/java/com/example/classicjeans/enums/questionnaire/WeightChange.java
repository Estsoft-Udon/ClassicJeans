package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 최근 체중 변화
@Getter
@AllArgsConstructor
public enum WeightChange {
    NONE("변화 없음", 0),
    WEIGHT_GAIN("체중 증가", -3),
    WEIGHT_LOSS("체중 감소", -5);

    private final String displayName;
    private final int impactScore;
}