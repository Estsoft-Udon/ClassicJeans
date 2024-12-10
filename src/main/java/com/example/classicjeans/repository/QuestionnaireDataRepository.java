package com.example.classicjeans.repository;

import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.QuestionnaireData;
import com.example.classicjeans.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface QuestionnaireDataRepository extends JpaRepository<QuestionnaireData, Long> {

    @Query("SELECT q FROM QuestionnaireData q WHERE (q.userId = :user AND q.familyId IS NULL) OR (q.familyId IN :familyInfos AND q.userId IS NULL)")
    List<QuestionnaireData> findByUserOrFamily(Users user, Collection<FamilyInfo> familyInfos);
}