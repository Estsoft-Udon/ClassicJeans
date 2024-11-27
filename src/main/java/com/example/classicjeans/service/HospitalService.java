package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.HospitalRequest;
import com.example.classicjeans.dto.response.HospitalResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class HospitalService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.serviceKey}")
    private String serviceKey;

    public HospitalService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public List<HospitalResponse> getHospitalList(HospitalRequest request) {
        String apiUrl = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("Q0", request.getCity()) // 시도
                .queryParam("Q1", request.getDistrict()) // 시군구
                .queryParam("QN", request.getHospitalName()) // 병원명
                .queryParam("ORD", "NAME") // 정렬 기준
                .queryParam("pageNo", "1") // 페이지 번호
                .queryParam("numOfRows", "10") // 한 페이지에 보여줄 병원 수
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);

        // API 응답 로그 출력
        System.out.println("API Response: " + response);

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items");

            List<HospitalResponse> hospitalList = new ArrayList<>();
            if (itemsNode.isArray()) {
                Iterator<JsonNode> elements = itemsNode.elements();
                while (elements.hasNext()) {
                    JsonNode itemNode = elements.next();
                    HospitalResponse hospital = objectMapper.treeToValue(itemNode, HospitalResponse.class);
                    hospitalList.add(hospital);
                }
            }

            return hospitalList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}