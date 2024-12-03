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
        Users user = usersRepository.findByLoginId(username);

        if (user == null || user.getIsDeleted()) {
            throw new UsernameNotFoundException("��� ����ڸ� ã�� �� �����ϴ�.: " + username);
        }

        // �α��� ���� �� lastLoginAt update
        user.updateLastLoginAt();
        usersRepository.save(user);

        return new CustomUserDetails(user);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // �̸����� �������� ����� ��ȸ
        Users user = usersRepository.findByEmail(email);

        if (user == null || user.getIsDeleted()) {
            throw new UsernameNotFoundException("��� ����ڸ� ã�� �� �����ϴ�.: " + email);
        }

        // �α��� ���� �� lastLoginAt update
        user.updateLastLoginAt();
        usersRepository.save(user);

        return new CustomUserDetails(user);
    }
}