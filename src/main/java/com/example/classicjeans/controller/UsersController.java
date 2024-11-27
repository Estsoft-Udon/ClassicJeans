package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.UsersResponse;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {
    private final UsersService usersService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UsersResponse> register(@RequestBody UsersRequest request) {
        return ResponseEntity.ok(new UsersResponse(usersService.register(request)));
    }


    @GetMapping("/users")
    public ResponseEntity<List<UsersResponse>> getAllUsers() {
        return ResponseEntity.ok(usersService.getUsers().stream().map(UsersResponse::new).toList());
    }

    // 유저 정보 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UsersResponse> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(new UsersResponse(usersService.findUserById(userId)));
    }

    // 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UsersResponse> updateUser(@PathVariable Long userId, @RequestBody UsersRequest usersRequest) {
        return ResponseEntity.ok(new UsersResponse(usersService.update(userId, usersRequest)));
    }

    // 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        usersService.delete(userId);
        return ResponseEntity.ok().build();
    }
}
