package com.example.classicjeans.service;

import com.example.classicjeans.repository.FamilyInfoRepository;
import com.example.classicjeans.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyInfoService {

    private final UsersRepository usersRepository;
    private final FamilyInfoRepository familyInfoRepository;

    public void saveFamily() {

    }
}
