package com.example.classicjeans.dto.request;

import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.questionnaire.MemoryChange;
import com.example.classicjeans.enums.questionnaire.CommonFrequency;

import lombok.Data;

@Data
public class AlanDementiaRequest {
    private Users user;
    private FamilyInfo family;
    // 인지 기능
    private MemoryChange memoryChange;  // 기억력 변화
    private CommonFrequency dailyConfusion;  // 일상 생활에서의 혼란
    private CommonFrequency problemSolvingChange;  // 문제 해결 능력 변화
    private CommonFrequency languageChange;  // 언어 사용 변화

    // 인지 기능 평가
    private boolean knowsDate;  // 날짜와 요일
    private boolean knowsLocation;  // 현재 위치
    private boolean remembersRecentEvents;  // 최근 사건 기억
    private CommonFrequency frequencyOfRepetition;  // 반복적 질문 빈도
    private CommonFrequency lostItemsFrequency;  // 물건 잃어버림 빈도

    // 일상 생활 능력 평가
    private CommonFrequency dailyActivityDifficulty;  // 일상 활동 난이도
    private CommonFrequency goingOutAlone;  // 혼자 외출 난이도
    private CommonFrequency financialManagementDifficulty;  // 금전 관리 능력

    // 행동 및 심리 증상 평가
    private CommonFrequency anxietyOrAggression;  // 불안, 공격성 빈도
    private CommonFrequency hallucinationOrDelusion;  // 환각/망상 빈도
    private CommonFrequency sleepPatternChange;  // 수면 패턴 변화 빈도

    // 건강 상태 및 병력
    private boolean hasChronicDiseases;  // 만성 질환 여부
    private boolean hasStrokeHistory;  // 뇌졸중 병력 여부
    private boolean hasFamilyDementia;  // 치매 가족력 여부

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("- 기억력 변화: ").append(memoryChange).append("\n")
                .append("- 일상 혼란: ").append(dailyConfusion).append("\n")
                .append("- 문제 해결 능력 변화: ").append(problemSolvingChange).append("\n")
                .append("- 언어 능력 변화: ").append(languageChange).append("\n")
                .append("- 날짜 인지 여부: ").append(knowsDate).append("\n")
                .append("- 위치 인지 여부: ").append(knowsLocation).append("\n")
                .append("- 최근 사건 기억 여부**: ").append(remembersRecentEvents).append("\n")
                .append("- 반복 빈도: ").append(frequencyOfRepetition).append("\n")
                .append("- 분실 빈도: ").append(lostItemsFrequency).append("\n")
                .append("- 일상 활동의 어려움: ").append(dailyActivityDifficulty).append("\n")
                .append("- 혼자 외출 가능 여부: ").append(goingOutAlone).append("\n")
                .append("- 재정 관리의 어려움: ").append(financialManagementDifficulty).append("\n")
                .append("- 불안 또는 공격성: ").append(anxietyOrAggression).append("\n")
                .append("- 환각 또는 망상: ").append(hallucinationOrDelusion).append("\n")
                .append("- 수면 패턴 변화: ").append(sleepPatternChange).append("\n")
                .append("- 만성 질환 유무: ").append(hasChronicDiseases).append("\n")
                .append("- 뇌졸중 병력: ").append(hasStrokeHistory).append("\n")
                .append("- 가족 중 치매 여부: ").append(hasFamilyDementia).append("\n\n")
                .append("다음 항목을 기반으로 아래 항목을 제공해 주세요:\n")
                .append("**종합 평가 (summaryEvaluation)**: [AI 응답] 요약 정리\n")
                .append("**개선 방법 (improvementSuggestions)**: [AI 응답]");

        return sb.toString();
    }
}