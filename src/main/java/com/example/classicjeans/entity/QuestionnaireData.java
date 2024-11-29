package com.example.classicjeans.entity;

import com.example.classicjeans.enums.Analysis;
import com.example.classicjeans.enums.questionnaire.*;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import static com.example.classicjeans.enums.Analysis.BASIC;

@Getter
@Entity
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

    @Enumerated(EnumType.STRING)
    private Analysis analysis = BASIC;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String height;

    @Column(nullable = false)
    private String weight;

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

    // 종합 평가 내용
    @ElementCollection
    @CollectionTable(name = "summary_evaluation", joinColumns = @JoinColumn(name = "dementia_data_id"))
    @Column(name = "evaluation", columnDefinition = "TEXT")
    private List<String> summaryEvaluation;

    // 개선 방법
    @ElementCollection
    @CollectionTable(name = "improvement_suggestions", joinColumns = @JoinColumn(name = "dementia_data_id"))
    @Column(name = "suggestion", columnDefinition = "TEXT")
    private List<String> improvementSuggestions;
}
