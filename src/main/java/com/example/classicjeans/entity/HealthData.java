package com.example.classicjeans.entity;

import com.example.classicjeans.enums.Analysis;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class HealthData {
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
    private Analysis analysis;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String content;
}
