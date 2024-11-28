package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 이름으로 중복 여부 확인
    boolean existsByName(String name);
}
