package com.example.classicjeans.dto.response;

import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyInfoResponse {
    private Long id;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Boolean isLunar;
    private String relationship;

    public static FamilyInfoResponse convertFamilyInfo(FamilyInfo familyInfo) {
        FamilyInfoResponse response = new FamilyInfoResponse();
        response.setId(familyInfo.getId());
        response.setName(familyInfo.getName());
        response.setGender(familyInfo.getGender());
        response.setIsLunar(familyInfo.getIsLunar());
        response.setDateOfBirth(familyInfo.getDateOfBirth());
        response.setRelationship(familyInfo.getRelationship());
        return response;
    }
}