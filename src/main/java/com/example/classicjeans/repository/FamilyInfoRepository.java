package com.example.classicjeans.repository;

import com.example.classicjeans.entity.FamilyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyInfoRepository extends JpaRepository<FamilyInfo, Long> {
}
