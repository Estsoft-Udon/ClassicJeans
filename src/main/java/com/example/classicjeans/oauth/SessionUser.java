package com.example.classicjeans.oauth;

import com.example.classicjeans.entity.Users;

public class SessionUser {
    private String email;

    public SessionUser(Users user) {
        if(user != null) {
            this.email = user.getEmail();
        }
    }
}
