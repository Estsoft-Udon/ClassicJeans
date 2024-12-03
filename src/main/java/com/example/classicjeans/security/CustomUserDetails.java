package com.example.classicjeans.security;


import com.example.classicjeans.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // �߰������� ��ƼƼ ���� ����
    private final Users user; // Users ��ƼƼ

    public Users getUser() {
        return user;
    }

    @Override
    public String getUsername() { // getLogInId() ��� getUsername() ���
        return user.getLoginId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return !user.getIsDeleted(); // Ȱ��ȭ ����
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  List.of(new SimpleGrantedAuthority("ROLE_" + user.getGrade().name()));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true; // ���� ���� ����
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // ���� ��� ����
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // �ڰ� ���� ���� ����
    }

    public String getNickname() {
        return user.getNickname();
    }
    public String getGrade(){
        return user.getGrade().name();
    }
}