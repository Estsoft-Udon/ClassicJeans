package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 최근 병원 방문 기록
@Getter
@AllArgsConstructor
public enum HospitalVisit {
    NONE("없음", 0),
    REGULAR_CHECKUP("정기 검사", 2),
    DISEASE_TREATMENT("질병 치료", -2),
    EMERGENCY("응급 치료", -3);

    private final String displayName;
    private final int impactScore;
}