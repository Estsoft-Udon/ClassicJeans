package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Bazi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlanBaziRepository extends JpaRepository<Bazi, Long> {
    Optional<Bazi> findByUserId(Long userId);
}
