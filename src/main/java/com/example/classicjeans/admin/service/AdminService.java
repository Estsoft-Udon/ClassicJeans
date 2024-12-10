package com.example.classicjeans.admin.service;

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

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UsersService usersService;
    private final UsersRepository usersRepository;

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
