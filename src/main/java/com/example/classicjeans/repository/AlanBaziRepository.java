package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlanBaziRepository extends JpaRepository<Bazi, Long> {
    List<Bazi> findByUserId(Long userId);
    boolean existsByUserAndDate(Users user, LocalDate date);
}