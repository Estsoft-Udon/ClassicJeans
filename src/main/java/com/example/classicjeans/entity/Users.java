package com.example.classicjeans.entity;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.enums.Gender;
import com.example.classicjeans.enums.Grade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "is_lunar", nullable = false)
    private Boolean isLunar = Boolean.FALSE;

    @Column(name = "hour_of_birth")
    private Integer hourOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLoginAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "unique_key")
    private String uniqueKey;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public Users(UsersRequest request) {
        this.loginId = request.getLoginId();
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.email = request.getEmail();
        this.grade = Grade.CHUNGBAZI;
        this.dateOfBirth = request.getDateOfBirth();
        this.isLunar = request.getIsLunar();
        this.hourOfBirth = request.getHourOfBirth();
        this.gender = request.getGender();
        if(request.getUniqueKey() != null) {
            this.uniqueKey = request.getUniqueKey();
        }
    }

    // 생년월일로 나이 계산
    @Transient
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }
}