package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.FamilyInfoRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.FamilyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class FamilyInfoService {

    private final UsersService usersService;
    private final FamilyInfoRepository familyInfoRepository;

    // 가족 정보 저장
    public List<FamilyInfoResponse> saveFamily(Long userId, List<FamilyInfoRequest> requests) {
        Users user = null;

        if(userId != null) {
            user = usersService.findUserById(userId);
        } else {
            user = usersService.findUserById(getLoggedInUser().getId());
        }

        List<FamilyInfo> familyInfoList = new ArrayList<>();
        for (FamilyInfoRequest request : requests) {
            FamilyInfo familyInfo = familyInfoRepository.save(new FamilyInfo(user, request.getName(), request.getGender(), request.getDateOfBirth(), request.getRelationship()));
            familyInfoList.add(familyInfo);
        }

        return familyInfoList.stream()
                .map(FamilyInfoResponse::convertFamilyInfo)
                .toList();
    }

    // 가족 정보 조회
    public List<FamilyInfoResponse> findFamilyByUserId(Long userId) {
        Users user = null;

        if(userId != null) {
            user = usersService.findUserById(userId);
        } else {
            user = usersService.findUserById(getLoggedInUser().getId());
        }

        List<FamilyInfo> familyList = familyInfoRepository.findByUserId(user);

        return familyList.stream()
                .map(FamilyInfoResponse::convertFamilyInfo)
                .toList();
    }

    // 가족 정보 단건 조회
    public FamilyInfo findFamily(Long familyId) {
        return familyInfoRepository.findById(familyId).orElse(null);
    }

    // 가족 정보 삭제
    public void deleteFamily(Long familyId) {
        FamilyInfo familyInfo = familyInfoRepository.findById(familyId)
                .orElseThrow(() -> new IllegalArgumentException("가족 정보를 찾을 수 없습니다. ID: " + familyId));
        familyInfoRepository.delete(familyInfo);
    }
}