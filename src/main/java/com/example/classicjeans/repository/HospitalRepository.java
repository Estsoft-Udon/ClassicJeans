package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 전화번호로 중복 체크
    boolean existsByPhone(String phone);

    // city와 district로 병원 검색 (페이지네이션 적용)
    Page<Hospital> findByCityAndDistrict(String city, String district, Pageable pageable);

    // city로 병원 검색 (페이지네이션 적용)
    Page<Hospital> findByCity(String city, Pageable pageable);

    // district로 병원 검색 (페이지네이션 적용)
    Page<Hospital> findByDistrict(String district, Pageable pageable);
}
