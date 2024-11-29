package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 전화번호로 중복 체크
    boolean existsByPhone(String phone);

    // city와 district로 병원 검색
    List<Hospital> findByCityAndDistrict(String city, String district);

    // city로 병원 검색
    List<Hospital> findByCity(String city);

    // district로 병원 검색
    List<Hospital> findByDistrict(String district);
}
