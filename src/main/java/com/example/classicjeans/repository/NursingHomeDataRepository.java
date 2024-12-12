package com.example.classicjeans.repository;

import com.example.classicjeans.entity.NursingHomeData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NursingHomeDataRepository extends JpaRepository<NursingHomeData, Long> {
    boolean existsByName(String name);
    Page<NursingHomeData> findAllByRegionAndSubRegion(String region, String subRegion, Pageable pageable);
    Page<NursingHomeData> findAllByNameContaining(String search, Pageable pageable);
}