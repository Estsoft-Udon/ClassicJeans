package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    List<Users> findByIsDeletedFalse();
    Users findByNameAndEmailAndIsDeletedFalse(String name, String email);
    Users findByLoginIdAndIsDeletedFalse(String loginId);
    Users findByLoginId(String loginId);
    Users findByEmail(String emailId);
    Users findByLoginIdAndEmailAndIsDeletedFalse(String loginId, String emailId);

    boolean existsByLoginIdIgnoreCase(String loginId);
    Page<Users> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);
    boolean existsByNicknameIgnoreCase(String nickname);
    boolean existsByEmailIgnoreCase(String email);
    Users findByEmailAndIsDeletedFalse(String email);

    Page<Users> findAllByIsDeletedFalse(Pageable pageable);
}
