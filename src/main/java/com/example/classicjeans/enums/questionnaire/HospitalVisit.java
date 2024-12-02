package com.example.classicjeans.enums.questionnaire;

// 최근 병원 방문 기록
public enum HospitalVisit {
    NONE("없음"),
    REGULAR_CHECKUP("정기 검사"),
    DISEASE_TREATMENT("질병 치료"),
    EMERGENCY("응급 치료");

    private final String displayName;

    HospitalVisit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}