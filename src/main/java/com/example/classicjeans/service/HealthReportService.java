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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<HealthReportResponse> getHealthReportList(Pageable pageable) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfo> familyInfos = familyInfoRepository.findByUserId(user);

        // 본인 및 가족의 모든 QuestionnaireData 및 DementiaData 조회 (페이지네이션 적용)
        Page<QuestionnaireData> questionnaireDataPage = questionnaireDataRepository.findByUserOrFamily(user, familyInfos, pageable);
        Page<DementiaData> dementiaDataPage = dementiaDataRepository.findByUserOrFamily(user, familyInfos, pageable);

        // HealthReportResponse로 변환
        List<HealthReportResponse> summaries = Stream.concat(
                        questionnaireDataPage.getContent().stream().map(data -> new HealthReportResponse(
                                "Questionnaire",
                                (data.getFamilyId() != null ? data.getFamilyId().getId() : data.getUserId().getId()),
                                (data.getFamilyId() != null ? data.getFamilyId().getName() : data.getUserId().getName()),
                                data.getDate()
                        )),
                        dementiaDataPage.getContent().stream().map(data -> new HealthReportResponse(
                                "Dementia",
                                (data.getFamilyId() != null ? data.getFamilyId().getId() : data.getUserId().getId()),
                                (data.getFamilyId() != null ? data.getFamilyId().getName() : data.getUserId().getName()),
                                data.getDate()
                        ))
                ).sorted(Comparator.comparing(HealthReportResponse::getDate).reversed())
                .toList();

        return new PageImpl<>(summaries, pageable, questionnaireDataPage.getTotalElements() + dementiaDataPage.getTotalElements());
    }

    // 단건 검사 기록 조회
    public Object getHealthReportById(Long reportId, String reportType) {
        return switch (reportType.toLowerCase()) {
            case "questionnaire" -> questionnaireDataRepository.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("없는 검사 기록"));
            case "dementia" -> dementiaDataRepository.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("없는 검사 기록"));
            default -> throw new IllegalArgumentException("제공하지 않은 검사 유형");
        };
    }
}