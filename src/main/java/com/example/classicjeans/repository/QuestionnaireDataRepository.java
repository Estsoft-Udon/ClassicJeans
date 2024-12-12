package com.example.classicjeans.repository;

import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.QuestionnaireData;
import com.example.classicjeans.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface QuestionnaireDataRepository extends JpaRepository<QuestionnaireData, Long> {

    // 기본 전체 목록 조회
    @Query("SELECT q FROM QuestionnaireData q WHERE (q.userId = :user AND q.familyId IS NULL) OR (q.familyId IN :familyInfos AND q.userId IS NULL)")
    List<QuestionnaireData> findByUserOrFamily(Users user, Collection<FamilyInfo> familyInfos);

    // 본인 목록 조회
    List<QuestionnaireData> findByUserId(Users user);

    // 해당 가족 조회
    List<QuestionnaireData> findByFamilyId(FamilyInfo familyInfo);

    // 최근 5개 조회
    @Query("SELECT q FROM QuestionnaireData q WHERE q.userId = :user AND q.id = " +
            "(SELECT MAX(q2.id) FROM QuestionnaireData q2 WHERE q2.userId = :user AND q2.date = q.date) " +
            "ORDER BY q.date DESC")
    List<QuestionnaireData> findTop5ByUserIdOrderByDateDesc(@Param("user") Users user, Pageable pageable);
}