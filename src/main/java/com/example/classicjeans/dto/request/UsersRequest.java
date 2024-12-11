package com.example.classicjeans.dto.request;

import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UsersRequest {
    private String loginId;

    private String name;

    private String nickname;

    private String email;

    private String password;

    private LocalDate dateOfBirth;

    private Boolean isLunar;

    private Integer hourOfBirth;

    private Gender gender;

    private String uniqueKey;

    public Users update(Users user) {
        if (password != null) {
            user.setPassword(password);
        }
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (hourOfBirth != null) {
            user.setHourOfBirth(hourOfBirth);
        }

        return user;
    }
}
