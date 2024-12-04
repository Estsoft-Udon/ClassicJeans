package com.example.classicjeans.repository;

import com.example.classicjeans.entity.QuestionnaireData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireDataRepository extends JpaRepository<QuestionnaireData, Long> {
}
