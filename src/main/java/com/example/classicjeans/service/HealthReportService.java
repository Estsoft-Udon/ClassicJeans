package com.example.classicjeans.service;

import com.example.classicjeans.dto.response.HealthReportResponse;
import com.example.classicjeans.entity.DementiaData;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.QuestionnaireData;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.DementiaDataRepository;
import com.example.classicjeans.repository.FamilyInfoRepository;
import com.example.classicjeans.repository.QuestionnaireDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Service
public class HealthReportService {
    private final QuestionnaireDataRepository questionnaireDataRepository;
    private final DementiaDataRepository dementiaDataRepository;
    private final UsersService usersService;
    private final FamilyInfoRepository familyInfoRepository;

    @Autowired
    public HealthReportService(QuestionnaireDataRepository questionnaireDataRepository, DementiaDataRepository dementiaDataRepository, UsersService usersService, FamilyInfoRepository familyInfoRepository) {
        this.questionnaireDataRepository = questionnaireDataRepository;
        this.dementiaDataRepository = dementiaDataRepository;
        this.usersService = usersService;
        this.familyInfoRepository = familyInfoRepository;
    }

    // 접속 중인 유저(본인 + 가족)의 검사 결과 목록 조회
    public List<HealthReportResponse> getHealthReportList() {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfo> familyInfos = familyInfoRepository.findByUserId(user);

        // 본인 및 가족의 모든 QuestionnaireData 및 DementiaData 조회
        List<QuestionnaireData> questionnaireData = questionnaireDataRepository.findByUserOrFamily(user, familyInfos);
        List<DementiaData> dementiaData = dementiaDataRepository.findByUserOrFamily(user, familyInfos);

        // HealthReportResponse로 변환
        List<HealthReportResponse> summaries = Stream.concat(
                        questionnaireData.stream().map(data -> new HealthReportResponse(
                                "Questionnaire",
                                (data.getFamilyId() != null ? data.getFamilyId().getId() : data.getUserId().getId()),
                                (data.getFamilyId() != null ? data.getFamilyId().getName() : data.getUserId().getName()),
                                data.getDate()
                        )),
                        dementiaData.stream().map(data -> new HealthReportResponse(
                                "Dementia",
                                (data.getFamilyId() != null ? data.getFamilyId().getId() : data.getUserId().getId()),
                                (data.getFamilyId() != null ? data.getFamilyId().getName() : data.getUserId().getName()),
                                data.getDate()
                        ))
                ).sorted(Comparator.comparing(HealthReportResponse::getDate).reversed())
                .toList();

        return summaries;
    }
}