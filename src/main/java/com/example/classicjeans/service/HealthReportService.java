package com.example.classicjeans.service;

import com.example.classicjeans.dto.response.HealthReportResponse;
import com.example.classicjeans.dto.response.HealthStatisticsResponse;
import com.example.classicjeans.entity.DementiaData;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.QuestionnaireData;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.DementiaDataRepository;
import com.example.classicjeans.repository.FamilyInfoRepository;
import com.example.classicjeans.repository.QuestionnaireDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Service
public class HealthReportService {
    private final QuestionnaireDataRepository questionnaireDataRepository;
    private final DementiaDataRepository dementiaDataRepository;
    private final UsersService usersService;
    private final FamilyInfoRepository familyInfoRepository;
    private final FamilyInfoService familyInfoService;
    private final AlanService alanService;

    @Autowired
    public HealthReportService(QuestionnaireDataRepository questionnaireDataRepository, DementiaDataRepository dementiaDataRepository, UsersService usersService, FamilyInfoRepository familyInfoRepository, FamilyInfoService familyInfoService, AlanService alanService) {
        this.questionnaireDataRepository = questionnaireDataRepository;
        this.dementiaDataRepository = dementiaDataRepository;
        this.usersService = usersService;
        this.familyInfoRepository = familyInfoRepository;
        this.familyInfoService = familyInfoService;
        this.alanService = alanService;
    }

    // 접속 중인 유저(본인 + 가족)의 검사 결과 목록 조회
    public Page<HealthReportResponse> getHealthReportList(int page, int size, String target) {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);
        List<FamilyInfo> familyInfos = familyInfoRepository.findByUserId(user);

        List<QuestionnaireData> questionnaireDataList;
        List<DementiaData> dementiaDataList;

        if (target.equals("user")) {
            questionnaireDataList = questionnaireDataRepository.findByUserId(user);
            dementiaDataList = dementiaDataRepository.findByUserId(user);
        } else if (target.equals("all")) {
            questionnaireDataList = questionnaireDataRepository.findByUserOrFamily(user, familyInfos);
            dementiaDataList = dementiaDataRepository.findByUserOrFamily(user, familyInfos);
        } else {
            Long familyId = Long.parseLong(target);
            FamilyInfo selectedFamilyInfo = familyInfoService.findFamily(familyId);
            questionnaireDataList = questionnaireDataRepository.findByFamilyId(selectedFamilyInfo);
            dementiaDataList = dementiaDataRepository.findByFamilyId(selectedFamilyInfo);
        }

        // HealthReportResponse로 변환 후 날짜 최신순으로 정렬
        List<HealthReportResponse> summaries = Stream.concat(
                        questionnaireDataList.stream().map(data -> new HealthReportResponse(
                                "Questionnaire",
                                data.getId(),
                                (data.getFamilyId() != null ? data.getFamilyId().getName() : data.getUserId().getName()),
                                data.getDate()
                        )),
                        dementiaDataList.stream().map(data -> new HealthReportResponse(
                                "Dementia",
                                data.getId(),
                                (data.getFamilyId() != null ? data.getFamilyId().getName() : data.getUserId().getName()),
                                data.getDate()
                        ))
                )
                .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
                .collect(Collectors.toList());

        // 전체 요소 수를 계산
        long totalElements = summaries.size();

        // 페이지네이션 처리된 결과를 균등하게 분할하여 반환
        int start = Math.min(page * size, summaries.size());
        int end = Math.min((start + size), summaries.size());
        List<HealthReportResponse> pagedSummaries = summaries.subList(start, end);

        return new PageImpl<>(pagedSummaries, PageRequest.of(page, size), totalElements);
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

    // 최근 기본 검사 결과 5건으로 bmi, 건강지수 가져오기
    public List<HealthStatisticsResponse> getRecent5QuestionnaireData() {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("date")));

        List<QuestionnaireData> recent5Data = questionnaireDataRepository.findTop5ByUserIdOrderByDateDesc(user, pageable);

        List<HealthStatisticsResponse> healthStatisticsList = new ArrayList<>();
        for (QuestionnaireData data : recent5Data) {
            double bmi = alanService.calculateBMI(data.getHeight(), data.getWeight());
            Double healthIndex = data.getHealthIndex();
            if (healthIndex == null) {
                healthIndex = 0.0;
            }
            LocalDate date = data.getDate();

            HealthStatisticsResponse response = new HealthStatisticsResponse(bmi, healthIndex, date);
            healthStatisticsList.add(response);
        }
        return healthStatisticsList;
    }

    // 최근 기본검사 결과 조회
    public QuestionnaireData getLatestHealthReport() {
        Long userId = getLoggedInUser().getId();
        Users user = usersService.findUserById(userId);

        return questionnaireDataRepository.findTopByOrderByDateDesc(user);
    }
}