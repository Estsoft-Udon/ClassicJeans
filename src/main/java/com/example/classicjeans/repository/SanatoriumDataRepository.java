package com.example.classicjeans.repository;

import com.example.classicjeans.entity.SanatoriumData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanatoriumDataRepository extends JpaRepository<SanatoriumData, Long> {
    boolean existsByName(String name);
}
