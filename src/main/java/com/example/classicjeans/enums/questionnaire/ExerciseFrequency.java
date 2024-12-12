package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 운동 빈도
@Getter
@AllArgsConstructor
public enum ExerciseFrequency {
    NONE("없음", -5),
    OCCASIONAL("가끔", 2),
    REGULAR("정기적", 5);

    private final String displayName;
    private final int impactScore;
}