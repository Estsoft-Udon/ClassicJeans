package com.example.classicjeans.security;

import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // 추가적으로 엔티티 접근 가능
    private final Users user;

    public Users getUser() {
        return user;
    }

    @Override
    public String getUsername() { // getLogInId() 대신 getUsername() 사용
        return user.getLoginId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return !user.getIsDeleted(); // 활성화 여부
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getGrade().name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    public String getNickname() {
        return user.getNickname();
    }

    public String getGrade() {
        return user.getGrade().name();
    }

    public LocalDate getBirthDate() {
        return user.getDateOfBirth();
    }

    public Gender getGender() {
        return user.getGender();
    }

    public Long getUserId() {
        return user.getId();
    }
}