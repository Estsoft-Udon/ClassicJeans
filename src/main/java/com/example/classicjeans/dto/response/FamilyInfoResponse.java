package com.example.classicjeans.dto.response;

import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FamilyInfoResponse {
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String relationship;

    public static FamilyInfoResponse convertFamilyInfo(FamilyInfo familyInfo) {
        FamilyInfoResponse response = new FamilyInfoResponse();
        response.setName(familyInfo.getName());
        response.setGender(familyInfo.getGender());
        response.setDateOfBirth(familyInfo.getDateOfBirth());
        response.setRelationship(familyInfo.getRelationship());
        return response;
    }
}
