package com.example.classicjeans.repository;

import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyInfoRepository extends JpaRepository<FamilyInfo, Long> {
    List<FamilyInfo> findByUserId(Users userId);
}