package com.example.classicjeans.entity;

import com.example.classicjeans.enums.questionnaire.*;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.util.DateFormatUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private FamilyInfo familyId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double weight;

    // 건강 상태
    @Enumerated(EnumType.STRING)
    private ChronicDisease chronicDisease;  // 만성 질환

    @Enumerated(EnumType.STRING)
    private HospitalVisit hospitalVisit;  // 병원 방문 여부

    @Enumerated(EnumType.STRING)
    private Medication currentMedication;  // 복용 약물

    // 생활 습관
    @Enumerated(EnumType.STRING)
    private SmokingStatus smokingStatus;  // 흡연 여부

    @Enumerated(EnumType.STRING)
    private AlcoholConsumption alcoholConsumption;  // 음주 빈도

    @Enumerated(EnumType.STRING)
    private ExerciseFrequency exerciseFrequency;  // 운동 빈도

    @Enumerated(EnumType.STRING)
    private DietPattern dietPattern;  // 식습관

    // 정신 건강
    @Enumerated(EnumType.STRING)
    private MoodStatus moodStatus;  // 최근 2주간 기분 상태

    @Enumerated(EnumType.STRING)
    private SleepPattern sleepPattern;  // 수면 패턴

    // 사회적 활동
    @Enumerated(EnumType.STRING)
    private IndependenceLevel independenceLevel;  // 자립 수준

    @Enumerated(EnumType.STRING)
    private SocialParticipation socialParticipation;  // 사회적 활동 참여

    // 가족력
    @Column(nullable = false)
    private boolean hasGeneticDisease;  // 가족 중 유전적 질환 여부

    // 추가 정보
    @Enumerated(EnumType.STRING)
    private WeightChange weightChange;  // 체중 변화

    @Column(nullable = false)
    private boolean hasAllergy;  // 알레르기 여부

    @Column(nullable = false)
    private String ageGroup;

    // 한국인 평균 값들
    @Column(nullable = false)
    private Double averageHeight;       // 한국인 평균 키

    @Column(nullable = false)
    private Double averageWeight;       // 한국인 평균 체중

    @Column(nullable = false)
    private Double smokingRate;         // 한국인 평균 흡연율

    @Column(nullable = false)
    private Double drinkingRate;        // 한국인 평균 음주율

    @Column(nullable = false)
    private Double exerciseRate;        // 한국인 평균 운동 실천율

    // 종합 평가 내용
    @OneToMany(mappedBy = "questionnaireData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SummaryEvaluation> summaryEvaluation;

    // 개선 방법
    @OneToMany(mappedBy = "questionnaireData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImprovementSuggestions> improvementSuggestions;

    // 건강지수
    @Column
    private Double healthIndex;

    @PrePersist
    public void setDate() {
        this.date = LocalDate.now();
    }

    public String getDate() {
        return this.date.format(DateFormatUtil.formatter);
    }

    public QuestionnaireData(
            Users userId, FamilyInfo familyId, int age, Gender gender, Double height,
            Double weight, ChronicDisease chronicDisease, HospitalVisit hospitalVisit,
            Medication currentMedication, SmokingStatus smokingStatus, AlcoholConsumption alcoholConsumption,
            ExerciseFrequency exerciseFrequency, DietPattern dietPattern, MoodStatus moodStatus,
            SleepPattern sleepPattern, IndependenceLevel independenceLevel, SocialParticipation socialParticipation,
            boolean hasGeneticDisease, WeightChange weightChange, boolean hasAllergy, String ageGroup,
            Double averageHeight, Double averageWeight, Double smokingRate, Double drinkingRate,
            Double exerciseRate, List<SummaryEvaluation> summaryEvaluation, List<ImprovementSuggestions> improvementSuggestions, Double healthIndex) {
        if (userId != null) {
            this.userId = userId;
        }
        if (familyId != null) {
            this.familyId = familyId;
        }
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.chronicDisease = chronicDisease;
        this.hospitalVisit = hospitalVisit;
        this.currentMedication = currentMedication;
        this.smokingStatus = smokingStatus;
        this.alcoholConsumption = alcoholConsumption;
        this.exerciseFrequency = exerciseFrequency;
        this.dietPattern = dietPattern;
        this.moodStatus = moodStatus;
        this.sleepPattern = sleepPattern;
        this.independenceLevel = independenceLevel;
        this.socialParticipation = socialParticipation;
        this.hasGeneticDisease = hasGeneticDisease;
        this.weightChange = weightChange;
        this.hasAllergy = hasAllergy;
        this.ageGroup = ageGroup;
        this.averageHeight = averageHeight;
        this.averageWeight = averageWeight;
        this.smokingRate = smokingRate;
        this.drinkingRate = drinkingRate;
        this.exerciseRate = exerciseRate;
        this.summaryEvaluation = summaryEvaluation;
        this.improvementSuggestions = improvementSuggestions;
        this.healthIndex = healthIndex;
    }
}