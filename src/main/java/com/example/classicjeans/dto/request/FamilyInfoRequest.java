package com.example.classicjeans.dto.request;

import com.example.classicjeans.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyInfoRequest {
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Boolean isLunar;
    private String relationship;
}