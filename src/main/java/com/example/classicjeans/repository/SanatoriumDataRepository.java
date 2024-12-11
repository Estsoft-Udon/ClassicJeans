package com.example.classicjeans.repository;

import com.example.classicjeans.entity.SanatoriumData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanatoriumDataRepository extends JpaRepository<SanatoriumData, Long> {
    boolean existsByName(String name);
    Page<SanatoriumData> findAllByRegionAndSubRegion(String region, String subRegion, Pageable pageable);
    Page<SanatoriumData> findAllByNameContaining(String search, Pageable pageable);
}