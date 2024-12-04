package com.example.classicjeans.dto.response;

import com.example.classicjeans.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FamilyInfoResponse {
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String relationship;
}
