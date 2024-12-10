package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 사회적 활동 참여 빈도
@Getter
@AllArgsConstructor
public enum SocialParticipation {
    NONE("없음", -5),
    OCCASIONAL("가끔", 2),
    FREQUENT("자주", 5);

    private final String displayName;
    private final int impactScore;
}