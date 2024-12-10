package com.example.classicjeans.repository;

import com.example.classicjeans.entity.HospitalData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepository<HospitalData, Long> {
    // 전화번호로 중복 체크
    boolean existsByPhone(String phone);

    // city와 district로 병원 검색 (페이지네이션 적용)
    Page<HospitalData> findByCityAndDistrict(String city, String district, Pageable pageable);

    // city로 병원 검색 (페이지네이션 적용)
    Page<HospitalData> findByCity(String city, Pageable pageable);

    // district로 병원 검색 (페이지네이션 적용)
    Page<HospitalData> findByDistrict(String district, Pageable pageable);

    // 병원명으로 병원 검색 (페이지네이션 적용)
    Page<HospitalData> findByNameContaining(String name, Pageable pageable);
}
