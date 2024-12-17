package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.SignupRequest;
import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.dto.response.UsersResponse;
import com.example.classicjeans.email.service.AuthService;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.classicjeans.util.SecurityUtil.*;

import java.util.Map;

@Tag(name = "유저 CRUD api", description = "회원가입/조회/수정/삭제")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;
    private final AuthService authService;

    // 회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<UsersResponse> register(@RequestBody UsersRequest request) {
        return ResponseEntity.ok(new UsersResponse(usersService.register(request)));
    }

    // 유저 정보 조회
    @Operation(summary = "유저 정보 단건 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UsersResponse> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(new UsersResponse(usersService.findUserById(userId)));
    }

    // 유저 정보 수정
    @Operation(summary = "유저 정보 수정")
    @PutMapping("/{userId}")
    public ResponseEntity<UsersResponse> updateUser(@PathVariable Long userId, @RequestBody UsersRequest usersRequest) {
        return ResponseEntity.ok(new UsersResponse(usersService.update(userId, usersRequest)));
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴 (softdelete)")
    @PostMapping("/withdrawal")
    public ResponseEntity<Void> doWithdrawal(@RequestBody Map<String, String> request) {
        String password = request.get("password");

        if (usersService.softDelete(getLoggedInUser().getId(), password)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 유저 정보 삭제
    @Operation(summary = "유저 정보 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        usersService.delete(userId);
        return ResponseEntity.ok().build();
    }

    // 아이디 찾기
    @Operation(summary = "ID 찾기")
    @PostMapping("/searchId")
    public ResponseEntity<String> findUserBySearchId(@RequestBody UsersRequest user) {
        Users foundUser = usersService.searchId(user.getName(), user.getEmail());
        return ResponseEntity.ok(foundUser.getLoginId());
    }

    // 회원 가입시 아이디 중복 및 패턴 검증 확인
    @Operation(summary = "회원 가입시 아이디 중복 체크 및 패턴 검증")
    @PostMapping("/checkId")
    public ResponseEntity<Boolean> checkId(@RequestBody Map<String, String> requestBody) {
        String loginId = requestBody.get("loginId");
        boolean isDuplicate = usersService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원 가입시 닉네임 중복 확인
    @Operation(summary = "회원 가입 시 닉네임 중복 체크")
    @PostMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> requestBody) {
        String nickname = requestBody.get("nickname");
        boolean isDuplicate = usersService.isLoginCheckNickname(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원 가입시 email 중복 확인
    @Operation(summary = "회원 가입 시 email 중복 체크")
    @PostMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean isDuplicate = usersService.isLoginCheckEmail(email);
        return ResponseEntity.ok(isDuplicate);
    }

    // 비밀번호 찾기 시 가입된 email 인지 확인
    @Operation(summary = "비밀번호 찾기 시 email과 loginId 찾기")
    @PostMapping("/checkEmailAndLoginId")
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

    // 현재 로그인된 유저의 닉네임 반환
    @Operation(summary = "현재 로그인된 유저의 닉네임 반환")
    @GetMapping("/nickname")
    public ResponseEntity<Map<String, String>> getNickname() {
        Users loggedInUser = getLoggedInUser();

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요합니다."));
        }

        Users user = usersService.findByLoginId(loggedInUser.getLoginId());

        if (user != null) {
            return ResponseEntity.ok(Map.of("nickname", user.getNickname()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "유저 정보가 없습니다."));
    }

    @Operation(summary = "회원가입 검증")
    @PostMapping("/validateSignup")
    public ResponseEntity<?> validateSignup(@RequestBody SignupRequest request) {
        boolean isIdDuplicate = usersService.isLoginIdDuplicate(request.getLoginId());
        boolean isIdValidate = usersService.isLoginIdValidate(request.getLoginId());
        boolean isNicknameDuplicate = usersService.isLoginCheckNickname(request.getNickname());
        boolean isNicknameValidate = usersService.isNicknameValidate(request.getNickname());
        boolean isEmailDuplicate = usersService.isLoginCheckEmail(request.getEmail());
        boolean isEmailVerified = authService.isEmailVerified(request.getEmail());
        boolean isPasswordValidate = usersService.isPasswordValidate(request.getPassword());
        boolean isConfirmPasswordValidate = usersService.isConfirmPasswordValidate(request.getPassword(), request.getConfirmPassword());

        if (isIdDuplicate || !isIdValidate) {
            return ResponseEntity.badRequest().body(Map.of("isValid", false, "message", "아이디를 확인해주세요."));
        }
        if (isNicknameDuplicate || isNicknameValidate) {
            return ResponseEntity.badRequest().body(Map.of("isValid", false, "message", "닉네임을 확인해주세요."));
        }
        if (isEmailDuplicate) {
            return ResponseEntity.badRequest().body(Map.of("isValid", false, "message", "이미 사용 중인 이메일입니다."));
        }
        if (!isEmailVerified) {
            return ResponseEntity.badRequest().body(Map.of("isValid", false, "message", "이메일 인증을 완료해주세요."));
        }
        if(!isPasswordValidate || !isConfirmPasswordValidate) {
            return ResponseEntity.badRequest().body(Map.of("isValid", false, "message", "비밀번호를 확인해주세요."));
        }

        return ResponseEntity.ok(Map.of("isValid", true));
    }
}