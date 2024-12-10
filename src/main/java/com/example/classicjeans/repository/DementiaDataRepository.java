package com.example.classicjeans.repository;

import com.example.classicjeans.entity.DementiaData;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DementiaDataRepository extends JpaRepository<DementiaData, Long> {

    // 기본 전체 목록 조회
    @Query("SELECT d FROM DementiaData d WHERE (d.userId = :user AND d.familyId IS NULL) OR (d.familyId IN :familyInfos AND d.userId IS NULL)")
    Page<DementiaData> findByUserOrFamily(Users user, Collection<FamilyInfo> familyInfos, Pageable pageable);

    // 본인 목록 조회
    Page<DementiaData> findByUserId(Users user, Pageable pageable);

    // 해당 가족 조회
    Page<DementiaData> findByFamilyId(FamilyInfo familyInfo, Pageable pageable);
}