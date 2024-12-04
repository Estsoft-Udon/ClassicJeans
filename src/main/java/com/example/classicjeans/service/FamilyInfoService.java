package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.FamilyInfoRequest;
import com.example.classicjeans.dto.response.FamilyInfoResponse;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.FamilyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyInfoService {

    private final UsersService usersService;
    private final FamilyInfoRepository familyInfoRepository;

    // 가족 정보 저장
    public FamilyInfoResponse saveFamily(Long userId, FamilyInfoRequest request) {
        // 추후에 로그인 중인 유저 정보로 변경 필요
        Users user = usersService.findUserById(8L);

        FamilyInfo familyInfo = familyInfoRepository.save(new FamilyInfo(user, request.getName(), request.getGender(), request.getDateOfBirth(), request.getRelationship()));
        return FamilyInfoResponse.convertFamilyInfo(familyInfo);
    }
}
