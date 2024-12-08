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
        return "입력한 건강 정보(1~7 항목)와 한국인 평균 데이터 비교하고, \n" +
                "1. **연령**: " + age + " (연령대: n0대)\n" +
                "2. **성별**: " + gender + " (성별: Gender)\n" +
                "3. **키**: " + height + " (한국인 남성 평균: cm)\n" +
                "4. **체중**: " + weight + " (한국인 남성 평균: kg)\n" +
                "5. **흡연 상태**: " + smokingStatus + " (한국인 흡연율: 약 %)\n" +
                "6. **음주 빈도**: " + alcoholConsumption + " (한국인 음주율: 약 %)\n" +
                "7. **운동 빈도**: " + exerciseFrequency + " (한국인 운동 빈도: 약 %)\n" +
                "\n  아래는 추가 정보" +
                "\n  만성 질환: " + chronicDisease +
                "\n  병원 방문: " + hospitalVisit +
                "\n  현재 복용 약물: " + currentMedication +
                "\n  식사 패턴: " + dietPattern +
                "\n  기분 상태: " + moodStatus +
                "\n  수면 패턴: " + sleepPattern +
                "\n  독립성 수준: " + independenceLevel +
                "\n  사회적 참여: " + socialParticipation +
                "\n  유전 질환: " + hasGeneticDisease +
                "\n  체중 변화: " + weightChange +
                "\n  알레르기: " + hasAllergy +
                "\n  이 정보를 바탕으로 자세히 분석하고 평가해서 설명을 상세하게 아래와 같은 2가지 항목을 List로 제공해줘" +
                "\n  **종합 평가 (summaryEvaluation)**: [이 값은 AI가 응답에서 제공]" +
                "\n  **개선 방법 (improvementSuggestions)**: [이 값은 AI가 응답에서 제공]" +
                "}";
    }
}
