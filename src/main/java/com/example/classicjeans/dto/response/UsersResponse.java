package com.example.classicjeans.dto.response;

import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Grade;
import com.example.classicjeans.util.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private Long id;
    private String loginId;
    private String name;
    private String nickname;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private Boolean isLunar;
    private Integer hourOfBirth;
    private String gender;
    private String createdAt;
    private String updatedAt;
    private String lastLoginAt;
    private Grade grade;

    public UsersResponse(Users user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.dateOfBirth = user.getDateOfBirth();
        this.isLunar = user.getIsLunar();
        this.hourOfBirth = user.getHourOfBirth();
        this.createdAt = user.getCreatedAt().format(DateFormatUtil.formatter);
        this.updatedAt = user.getUpdatedAt().format(DateFormatUtil.formatter);
        if(user.getLastLoginAt() != null) {
            this.lastLoginAt = user.getLastLoginAt().format(DateFormatUtil.formatter);
        }
        if(user.getGrade() != null) {
            this.grade = user.getGrade();
        }
    }
}