package com.example.classicjeans.addresscode.repository;

import com.example.classicjeans.addresscode.entity.AddressCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressCodeRepository extends JpaRepository<AddressCode, Integer> {
    boolean existsByCode(String code);

    AddressCode findByCode(String code);
}