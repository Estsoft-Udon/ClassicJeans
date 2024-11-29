package com.example.classicjeans.entity;

import com.example.classicjeans.enums.Analysis;
import com.example.classicjeans.enums.questionnaire.MemoryChange;
import com.example.classicjeans.enums.questionnaire.CommonFrequency;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import static com.example.classicjeans.enums.Analysis.DEMENTIA;

@Getter
@Entity
public class DementiaData {
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
    private Analysis analysis = DEMENTIA;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private MemoryChange memoryChange; // 기억력 변화

    @Enumerated(EnumType.STRING)
    private CommonFrequency dailyConfusion; // 일상 혼란

    @Enumerated(EnumType.STRING)
    private CommonFrequency problemSolvingChange; // 문제 해결 능력 변화

    @Enumerated(EnumType.STRING)
    private CommonFrequency languageChange; // 언어 사용 변화

    @Column(nullable = false)
    private boolean knowsDate; // 날짜와 요일 인지 여부

    @Column(nullable = false)
    private boolean knowsLocation; // 현재 위치 인지 여부

    @Column(nullable = false)
    private boolean remembersRecentEvents; // 최근 사건 기억 여부

    @Enumerated(EnumType.STRING)
    private CommonFrequency frequencyOfRepetition; // 반복적 질문 빈도

    @Enumerated(EnumType.STRING)
    private CommonFrequency lostItemsFrequency; // 물건 잃어버림 빈도

    @Enumerated(EnumType.STRING)
    private CommonFrequency dailyActivityDifficulty; // 일상 활동 난이도

    @Enumerated(EnumType.STRING)
    private CommonFrequency goingOutAlone; // 혼자 외출 난이도

    @Enumerated(EnumType.STRING)
    private CommonFrequency financialManagementDifficulty; // 금전 관리 난이도

    @Enumerated(EnumType.STRING)
    private CommonFrequency anxietyOrAggression; // 불안, 공격성 빈도

    @Enumerated(EnumType.STRING)
    private CommonFrequency hallucinationOrDelusion; // 환각/망상 빈도

    @Enumerated(EnumType.STRING)
    private CommonFrequency sleepPatternChange; // 수면 패턴 변화 빈도

    @Column(nullable = false)
    private boolean hasChronicDiseases; // 만성 질환 여부

    @Column(nullable = false)
    private boolean hasStrokeHistory; // 뇌졸중 병력 여부

    @Column(nullable = false)
    private boolean hasFamilyDementia; // 치매 가족력 여부

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
