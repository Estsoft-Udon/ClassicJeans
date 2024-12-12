package com.example.classicjeans.dto.request;

import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.questionnaire.*;
import lombok.Data;

@Data
public class AlanQuestionnaireRequest {
    private Users user;
    private FamilyInfo family;

    private Double height;
    private Double weight;

    // 건강 상태
    private ChronicDisease chronicDisease;  // 현재 앓고 있는 만성 질환
    private HospitalVisit hospitalVisit;  // 최근 병원 방문 여부
    private Medication currentMedication;  // 현재 복용 약물

    // 생활 습관
    private SmokingStatus smokingStatus;  // 흡연 여부
    private AlcoholConsumption alcoholConsumption;  // 음주 빈도
    private ExerciseFrequency exerciseFrequency;  // 운동 빈도
    private DietPattern dietPattern;  // 식습관

    // 정신 건강
    private MoodStatus moodStatus;  // 최근 2주간 기분 상태
    private SleepPattern sleepPattern;  // 수면 패턴

    // 사회적 활동
    private IndependenceLevel independenceLevel;  // 자립 수준
    private SocialParticipation socialParticipation;  // 사회적 활동 참여

    // 가족력
    private boolean hasGeneticDisease;  // 가족 중 유전적 질환 여부

    // 추가 정보
    private WeightChange weightChange;  // 최근 체중 변화
    private boolean hasAllergy;  // 알레르기 여부

    @Override
    public String toString() {
        int age = 0;
        String gender = "UNKNOWN";

        if (user != null) {
            age = user.getAge();
            gender = user.getGender().toString();
        } else if (family != null) {
            age = family.getAge();
            gender = family.getGender().toString();
        }

        String ageGroup = (age / 10) * 10 + "대";
        // 출력 문자열 생성
        StringBuilder sb = new StringBuilder();
        sb.append("사용자의 건강 정보를 한국인 평균 데이터와 비교합니다:\n")
                .append("1. **연령**: ").append(age).append(" (연령대: ").append(ageGroup).append(")\n")
                .append("2. **성별**: ").append(gender).append("\n")
                .append("3. **키**: ").append(height).append(" (" + ageGroup + " ").append(gender.equals("MALE") ? "남성" : "여성").append(" 평균: 약 cm)\n")
                .append("4. **체중**: ").append(weight).append(" (" + ageGroup + " ").append(gender.equals("MALE") ? "남성" : "여성").append(" 평균: 약 kg)\n")
                .append("5. **흡연 상태**: ").append(smokingStatus).append(" (한국인 ").append(gender.equals("MALE") ? "남성" : "여성").append(" 흡연율: 약 %)\n")
                .append("6. **음주 빈도**: ").append(alcoholConsumption).append(" (한국인 ").append(gender.equals("MALE") ? "남성" : "여성").append(" 음주율: 약 %)\n")
                .append("7. **운동 빈도**: ").append(exerciseFrequency).append(" (한국인 ").append(gender.equals("MALE") ? "남성" : "여성").append(" 평균: 약 %)\n\n")
                .append("추가 정보:\n")
                .append("- 만성 질환: ").append(chronicDisease).append("\n")
                .append("- 병원 방문: ").append(hospitalVisit).append("\n")
                .append("- 현재 복용 약물: ").append(currentMedication).append("\n")
                .append("- 식사 패턴: ").append(dietPattern).append("\n")
                .append("- 기분 상태: ").append(moodStatus).append("\n")
                .append("- 수면 패턴: ").append(sleepPattern).append("\n")
                .append("- 독립성 수준: ").append(independenceLevel).append("\n")
                .append("- 사회적 참여: ").append(socialParticipation).append("\n")
                .append("- 유전 질환: ").append(hasGeneticDisease).append("\n")
                .append("- 체중 변화: ").append(weightChange).append("\n")
                .append("- 알레르기: ").append(hasAllergy).append("\n\n")
                .append("다음 항목을 기반으로 상세한 분석 및 평가를 요청합니다:\n")
                .append("**종합 평가 (summaryEvaluation)**: [AI 응답]\n")
                .append("**개선 방법 (improvementSuggestions)**: [AI 응답]");

        return sb.toString();
    }
}