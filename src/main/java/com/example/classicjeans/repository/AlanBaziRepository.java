package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Bazi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlanBaziRepository extends JpaRepository<Bazi, Long> {
    List<Bazi> findByUserId(Long userId);
}
