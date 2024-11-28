package com.example.classicjeans.service;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public HospitalService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    // 병원 목록 조회 (페이지네이션)
    public List<HospitalResponse> getHospitalList(int pageNo, int numOfRows) throws IOException, URISyntaxException {
        String url = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";
        String serviceKey = "PCnXo01ezwgVrAtBI1kDSkxM5DUmKQhd1Ymna75IirQaRHIkp9xdqTw0uVOV9sPUcaLd%2BS0SxuZLTm%2BA2DMppQ%3D%3D";
        String baseUrl = url +
                "?serviceKey=" + serviceKey +
                "&pageNo=" + pageNo +  // 페이지 번호
                "&numOfRows=" + numOfRows; // 한 페이지 항목 수

        URI uri = new URI(baseUrl);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        String response = responseEntity.getBody();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            List<HospitalResponse> hospitalList = new ArrayList<>();
            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    String name = itemNode.path("dutyName").asText("");  // 병원 이름
                    String address = itemNode.path("dutyAddr").asText("");  // 상세 주소
                    String phone = itemNode.path("dutyTel1").asText("");  // 전화번호
                    String latitude = String.valueOf(itemNode.path("wgs84Lat").asDouble(0.0));  // 위도
                    String longitude = String.valueOf(itemNode.path("wgs84Lon").asDouble(0.0));  // 경도

                    String[] addressParts = address.split(" ");
                    String city = addressParts.length > 0 ? addressParts[0] : "";
                    String district = addressParts.length > 1 ? addressParts[1] : "";

                    HospitalResponse hospital = new HospitalResponse(name, address, phone, latitude, longitude, city, district);
                    hospitalList.add(hospital);
                }
            }

            return hospitalList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 지역으로 필터링된 병원 목록 조회 (페이지네이션 적용)
    public List<HospitalResponse> getFilteredHospitalList(String city, String district, int pageNo, int numOfRows) throws IOException, URISyntaxException {
        List<HospitalResponse> allHospitals = getHospitalList(pageNo, numOfRows); // 페이지네이션 적용
        List<HospitalResponse> filteredHospitals = new ArrayList<>();

        // 병원 데이터를 필터링하는 로직
        for (HospitalResponse hospital : allHospitals) {
            // city와 district가 null이 아니고, 정확하게 일치하는 경우에만 필터링
            if ((hospital.getCity() != null && hospital.getCity().equals(city)) &&
                    (hospital.getDistrict() != null && hospital.getDistrict().equals(district))) {
                filteredHospitals.add(hospital);
            }
        }
        return filteredHospitals;
    }

    // 총 페이지 수 계산
    public int getTotalPages(int numOfRows) throws IOException, URISyntaxException {
        String url = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";
        String serviceKey = "PCnXo01ezwgVrAtBI1kDSkxM5DUmKQhd1Ymna75IirQaRHIkp9xdqTw0uVOV9sPUcaLd%2BS0SxuZLTm%2BA2DMppQ%3D%3D";
        String baseUrl = url +
                "?serviceKey=" + serviceKey +
                "&pageNo=1" +  // 첫 번째 페이지로 요청
                "&numOfRows=" + numOfRows; // 한 페이지 항목 수

        URI uri = new URI(baseUrl);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        String response = responseEntity.getBody();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode totalCountNode = rootNode.path("response").path("body").path("totalCount");
            int totalCount = totalCountNode.asInt();

            // 총 페이지 수 계산
            return (int) Math.ceil((double) totalCount / numOfRows);
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // 오류 발생 시 0 반환
        }
    }
}