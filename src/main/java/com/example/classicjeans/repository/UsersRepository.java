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
    Users findByNameAndEmail(String name, String email);
    Users findByLoginId(String loginId);

    List<Users> findByNameContainingAndIsDeletedFalse(String name);

    boolean existsByLoginIdIgnoreCase(String loginId);
    Page<Users> findByNameContainingAndIsDeleted(String search, Pageable pageable);
    boolean existsByNicknameIgnoreCase(String nickname);
    boolean existsByEmailIgnoreCase(String email);
    Users findByEmail(String email);

    Page<Users> findAllByIsDeletedFalse(Pageable pageable);
}
