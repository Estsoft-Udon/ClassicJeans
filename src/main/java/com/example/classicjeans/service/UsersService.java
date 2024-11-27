package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    public Users register(UsersRequest request) {
        Users user = new Users(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return usersRepository.save(user);
    }

    // 전체 조회
    public List<Users> getUsers() {
        return usersRepository.findByIsDeletedFalse();
    }

    // 유저 단건 조회
    public Users findUserById(Long id) {
        Users user = usersRepository.findById(id).orElse(null);

        if(user == null || user.getIsDeleted()) {
            return null;
        }

        return user;
    }

    // 유저 정보 수정
    public Users update(Long userId, UsersRequest request) {
        Users user = findUserById(userId);

        if(user == null) {
            return null;
        }

        return usersRepository.save(request.update(user));
    }

    // soft Delete
    public Users softDelete(Long id) {
        Users user = usersRepository.findById(id).orElse(null);

        if(user == null) {
            return null;
        }

        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        return usersRepository.save(user);
    }

    // Delete
    public boolean delete(Long id) {
        if(findUserById(id) == null) {
            return false;
        }
        usersRepository.deleteById(id);

        return true;
    }
}
