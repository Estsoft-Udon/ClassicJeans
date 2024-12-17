package com.example.classicjeans.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String loginId;
    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;
}
