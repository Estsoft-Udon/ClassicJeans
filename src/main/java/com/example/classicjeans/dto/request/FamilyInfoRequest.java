package com.example.classicjeans.dto.request;

import com.example.classicjeans.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FamilyInfoRequest {
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String relationship;
}