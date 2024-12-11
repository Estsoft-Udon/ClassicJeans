package com.example.classicjeans.security;

import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersDetailService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByLoginIdAndIsDeletedFalse(username);

        if (user == null || user.getIsDeleted()) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.: " + username);
        }

        // 로그인 성공 시 lastLoginAt update
        user.updateLastLoginAt();
        usersRepository.save(user);

        return new CustomUserDetails(user);
    }

    public UserDetails loadUserByEmail(String email)  {
        // 이메일을 기준으로 사용자 조회
        Users user = usersRepository.findByEmail(email);

        if (user == null || user.getIsDeleted()) {
            return null;
        }

        // 로그인 성공 시 lastLoginAt update
        user.updateLastLoginAt();
        usersRepository.save(user);

        return new CustomUserDetails(user);
    }

    public UserDetails loadUserByUniqueKey(String uniqueKey)  {
        Users user = usersRepository.findByUniqueKeyAndIsDeletedFalse(uniqueKey);

        if(user == null) {
            return null;
        }

        return new CustomUserDetails(user);
    }
}