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
        return "AlanDementiaRequest {" +
                "\n  memoryChange=" + memoryChange +
                ", \n  dailyConfusion=" + dailyConfusion +
                ", \n  problemSolvingChange=" + problemSolvingChange +
                ", \n  languageChange=" + languageChange +
                ", \n  knowsDate=" + knowsDate +
                ", \n  knowsLocation=" + knowsLocation +
                ", \n  remembersRecentEvents=" + remembersRecentEvents +
                ", \n  frequencyOfRepetition=" + frequencyOfRepetition +
                ", \n  lostItemsFrequency=" + lostItemsFrequency +
                ", \n  dailyActivityDifficulty=" + dailyActivityDifficulty +
                ", \n  goingOutAlone=" + goingOutAlone +
                ", \n  financialManagementDifficulty=" + financialManagementDifficulty +
                ", \n  anxietyOrAggression=" + anxietyOrAggression +
                ", \n  hallucinationOrDelusion=" + hallucinationOrDelusion +
                ", \n  sleepPatternChange=" + sleepPatternChange +
                ", \n  hasChronicDiseases=" + hasChronicDiseases +
                ", \n  hasStrokeHistory=" + hasStrokeHistory +
                ", \n  hasFamilyDementia=" + hasFamilyDementia +
                "\n  이 정보를 바탕으로 자세히 분석하고 평가해서 설명을 상세하게 아래와 같은 2가지 항목을 List로 제공해줘" +
                "\n  **종합 평가 (summaryEvaluation)**: [이 값은 AI가 응답에서 제공]" +
                "\n  **개선 방법 (improvementSuggestions)**: [이 값은 AI가 응답에서 제공]" +
                "}";
    }
}