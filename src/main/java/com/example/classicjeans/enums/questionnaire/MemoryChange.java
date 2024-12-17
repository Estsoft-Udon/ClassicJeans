package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 기억력 변화
@Getter
@AllArgsConstructor
public enum MemoryChange {
    NO_CHANGE("변화 없음"),
    SOMETIMES("가끔"),
    OFTEN("자주");

    private final String displayName;
}