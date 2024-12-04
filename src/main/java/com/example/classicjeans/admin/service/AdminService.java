package com.example.classicjeans.admin.service;

import com.example.classicjeans.dto.response.UsersResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.enums.Grade;
import com.example.classicjeans.repository.UsersRepository;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UsersService usersService;
    private final UsersRepository usersRepository;

    // 관리자 로그인
    public Users adminLogin(String loginId, String password) {
        Users adminUser = usersService.findByLoginId(loginId);
        if (adminUser == null || !adminUser.getPassword().equals(password) || !adminUser.getGrade()
                .equals(Grade.ADMIN)) {
            throw new IllegalArgumentException("관리자가 아님");
        }
        return adminUser;
    }

    // 회원 관리 리스트
    public List<UsersResponse> getAllUsers() {
        List<Users> users = usersService.getUsers();
        return users.stream()
                .map(UsersResponse::new)
                .toList();
    }

    // 회원 이름으로 검색 (동명이인 가능), 이름에 포함된 글자도 출력가능
    public Page<Users> getUsersSearchName(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usersRepository.findByNameContainingAndIsDeletedFalse(search, pageable);
    }

    // 검색 검색 및 select 구현
    public Page<Users> getFilteredUsers(int page, int size, String sortOption, String keyword) {
        Pageable pageable = PageRequest.of(page, size, determineSortOrder(sortOption));

        if (keyword != null && !keyword.isEmpty()) {
            return usersRepository.findByNameContainingAndIsDeletedFalse(keyword, pageable);
        } else {
            return usersRepository.findAllByIsDeletedFalse(pageable);
        }
    }

    public void updateUserGrade(Long id, Grade grade) {
        Users user = usersService.findById(id);
        user.setGrade(grade);
        usersRepository.save(user);
    }

    private Sort determineSortOrder(String sortOption) {
        return switch (sortOption) {
            case "recent" -> Sort.by("createdAt").descending();
            case "abc" -> Sort.by("name").ascending();
            default -> Sort.unsorted();
        };
    }
}
