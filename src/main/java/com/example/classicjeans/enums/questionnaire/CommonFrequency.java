package com.example.classicjeans.enums.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 빈도와 정도를 나타내는 공통 Enum
@Getter
@AllArgsConstructor
public enum CommonFrequency {
    NONE("없음"),
    SOMETIMES("가끔"),
    OFTEN("자주");

    private final String displayName;
}