package com.example.classicjeans.repository;

import com.example.classicjeans.entity.DementiaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DementiaDataRepository extends JpaRepository<DementiaData, Long> {
}
