package com.example.classicjeans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ImprovementSuggestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dementia_data_id")
    private DementiaData dementiaData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire_data_id")
    private QuestionnaireData questionnaireData;

    @Column(columnDefinition = "TEXT")
    private String suggestion;
}
