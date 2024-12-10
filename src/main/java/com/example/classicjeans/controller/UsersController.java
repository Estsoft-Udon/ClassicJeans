package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.UsersResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.example.classicjeans.util.SecurityUtil.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UsersResponse> register(@RequestBody UsersRequest request) {
        return ResponseEntity.ok(new UsersResponse(usersService.register(request)));
    }


    @GetMapping("/all")
    public ResponseEntity<List<UsersResponse>> getAllUsers() {
        return ResponseEntity.ok(usersService.getUsers().stream().map(UsersResponse::new).toList());
    }

    // 유저 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UsersResponse> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(new UsersResponse(usersService.findUserById(userId)));
    }

    // 수정
    @PutMapping("/{userId}")
    public ResponseEntity<UsersResponse> updateUser(@PathVariable Long userId, @RequestBody UsersRequest usersRequest) {
        return ResponseEntity.ok(new UsersResponse(usersService.update(userId, usersRequest)));
    }

    // 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        usersService.delete(userId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/search-id")
    public ResponseEntity<String> findUserBySearchId(@RequestBody UsersRequest user) {
        Users foundUser = usersService.searchId(user.getName(), user.getEmail());
        return ResponseEntity.ok(foundUser.getLoginId());
    }

    // 회원가입시 아이디 중복체크
    @PostMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestBody Map<String, String> requestBody) {
        String loginId = requestBody.get("loginId");
        boolean isDuplicate = usersService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원가입시 닉네임 중복체크
    @PostMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> requestBody) {
        String nickname = requestBody.get("nickname");
        boolean isDuplicate = usersService.isLoginCheckNickname(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원가입시 email 중복체크
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean isDuplicate = usersService.isLoginCheckEmail(email);
        return ResponseEntity.ok(isDuplicate);
    }

    // 비밀번호 찾기 시 가입된 email인지 체크
    @PostMapping("/check-email-and-login-id")
    public ResponseEntity<Map<String, String>> checkEmailAndLoginId(@RequestBody Map<String, String> requestBody,
                                                                    HttpSession session) {
        String loginId = requestBody.get("loginId");
        String email = requestBody.get("email");

        try {
            Users foundUser = usersService.findByLoginIdAndEmail(loginId, email);

            if (foundUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자나 이메일을 찾을 수 없습니다."));
            }
            session.setAttribute("loginId", loginId);

            return ResponseEntity.ok(Map.of("message", "인증 코드가 이메일로 전송되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류가 발생했습니다."));
        }
    }

    // 회원탈퇴
    @PostMapping("/withdrawal")
    public ResponseEntity<Void> doWithdrawal(@RequestBody Map<String, String> request) {
        String password = request.get("password");

        if(usersService.softDelete(getLoggedInUser().getId(), password)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
