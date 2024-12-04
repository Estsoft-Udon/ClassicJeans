package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.UsersRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public Boolean softDelete(Long id, String password) {
        Users user = usersRepository.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        // 비밀번호가 인코딩된 비밀번호이거나 원본 비밀번호일 경우(어드민) 처리
        if (passwordEncoder.matches(password, user.getPassword()) || password.equals(user.getPassword())) {
            user.setIsDeleted(true);
            user.setDeletedAt(LocalDateTime.now());
            usersRepository.save(user);
            return true;
        }

        return false;
    }

    // 회원정보삭제
    public boolean delete(Long id) {
        if(findUserById(id) == null) {
            return false;
        }
        usersRepository.deleteById(id);

        return true;
    }

    // 아이디 찾기
    public Users searchId(String name, String email) {
        return usersRepository.findByNameAndEmailAndIsDeletedFalse(name, email);
    }

    // 회원가입시 아이디 중복체크
    public boolean isLoginIdDuplicate(String loginId) {
        return usersRepository.existsByLoginIdIgnoreCase(loginId);
    }

    // 회원가입시 닉네임 중복체크
    public boolean isLoginCheckNickname(String nickname) {
        return usersRepository.existsByNicknameIgnoreCase(nickname);
    }

    // 회원가입시 email 중복체크
    public boolean isLoginCheckEmail(String email) {
        return usersRepository.existsByEmailIgnoreCase(email);
    }

    // 비밀번호 변경
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        usersRepository.save(user);
        return true;
    }

    public Users findByLoginId(String loginId) {
        return usersRepository.findByLoginIdAndIsDeletedFalse(loginId);
    }


    public Users findById(Long userId) {
        return usersRepository.findByIdAndIsDeletedFalse(userId);
    }

    public void changePasswordAfterFind(String loginId, String newPassword) {
        Users user = usersRepository.findByLoginId(loginId);
        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }

    public Users findByLoginIdAndEmail(String loginId, String email) {

        return usersRepository.findByLoginIdAndEmailAndIsDeletedFalse(loginId, email);
    }
}
