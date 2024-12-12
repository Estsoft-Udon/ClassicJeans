package com.example.classicjeans.entity;

import com.example.classicjeans.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FamilyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column
    private String relationship;

    // 생년월일로 나이 계산
    @Transient
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    public FamilyInfo(Users userId, String name, Gender gender, LocalDate dateOfBirth, String relationship) {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.relationship = relationship;
    }
}