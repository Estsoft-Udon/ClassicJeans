package com.example.classicjeans.admin.controller;

import com.example.classicjeans.admin.service.AdminService;
import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.UsersResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final UsersService usersService;

    @PostMapping("/login")
    public ResponseEntity<UsersResponse> login(@RequestBody UsersRequest request) {
        Users admin = adminService.adminLogin(request.getLoginId(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsersResponse(admin));
    }

    // 관리자 회원 관리 리스트
    @GetMapping("/users")
    public ResponseEntity<List<UsersResponse>> getAllUsers() {
        List<UsersResponse> userList = adminService.getAllUsers();
        return ResponseEntity.ok(userList);
    }
    // 회원의 이름으로 검색하기
    @GetMapping("/member_list")
    public ResponseEntity<Page<UsersResponse>> searchUsersByName(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 실제 서비스 호출 및 필터링된 결과 반환
        Page<UsersResponse> usersResponsePage = adminService.getUsersSearchName(keyword, page, size)
                .map(UsersResponse::new);

        // 필터링된 데이터가 있는지 확인
        if (usersResponsePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(usersResponsePage); // 데이터 없음
        }

        return ResponseEntity.ok(usersResponsePage); // 필터링된 데이터 반환
    }

}
