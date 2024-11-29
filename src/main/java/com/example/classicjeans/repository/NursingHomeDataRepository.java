package com.example.classicjeans.repository;

import com.example.classicjeans.entity.NursingHomeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NursingHomeDataRepository extends JpaRepository<NursingHomeData, Long> {
    boolean existsByName(String name);
}
