package com.example.classicjeans.dto.request;

import lombok.Data;

@Data
public class AlanDementiaRequest {
    // 인지 기능
    private MemoryChange memoryChange;  // 기억력 변화
    private DailyConfusion dailyConfusion;  // 일상 생활에서의 혼란
    private ProblemSolvingChange problemSolvingChange;  // 문제 해결 능력 변화
    private LanguageChange languageChange;  // 언어 사용 변화

    // 인지 기능 평가
    private boolean knowsDate;  // 날짜와 요일
    private boolean knowsLocation;  // 현재 위치
    private boolean remembersRecentEvents;  // 최근 사건 기억
    private Repetition frequencyOfRepetition;  // 반복적 질문 빈도
    private LostItems lostItemsFrequency;  // 물건 잃어버림 빈도

    // 일상 생활 능력 평가
    private Difficulty dailyActivityDifficulty;  // 일상 활동 난이도
    private Difficulty goingOutAlone;  // 혼자 외출 난이도
    private Difficulty financialManagementDifficulty;  // 금전 관리 능력

    // 행동 및 심리 증상 평가
    private Frequency anxietyOrAggression;  // 불안, 공격성 빈도
    private Frequency hallucinationOrDelusion;  // 환각/망상 빈도
    private Frequency sleepPatternChange;  // 수면 패턴 변화 빈도

    // 건강 상태 및 병력
    private boolean hasChronicDiseases;  // 만성 질환 여부
    private boolean hasStrokeHistory;  // 뇌졸중 병력 여부
    private boolean hasFamilyDementia;  // 치매 가족력 여부

    // Enum으로 선택지 정의
    public enum MemoryChange { NO_CHANGE, SOMETIMES, OFTEN }
    public enum DailyConfusion { NONE, SOMETIMES, OFTEN }
    public enum ProblemSolvingChange { NONE, SOMETIMES, OFTEN }
    public enum LanguageChange { NONE, SOMETIMES, OFTEN }
    public enum Repetition { NONE, SOMETIMES, OFTEN }
    public enum LostItems { NONE, SOMETIMES, OFTEN }
    public enum Difficulty { NONE, SOMETIMES, OFTEN }
    public enum Frequency { NONE, SOMETIMES, OFTEN }

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
                "\n  종합평가, 개선방법 상세히 알려줘" +
                "\n  **종합 평가 (summaryEvaluation)**: [이 값은 AI가 응답에서 제공]" +
                "\n  **개선 방법 (improvementSuggestions)**: [이 값은 AI가 응답에서 제공]" +
                "}";
    }
}