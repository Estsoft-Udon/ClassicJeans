package com.example.classicjeans.service;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.entity.HospitalData;
import com.example.classicjeans.repository.HospitalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final HospitalRepository hospitalRepository;

    public HospitalService(ObjectMapper objectMapper, HospitalRepository hospitalRepository) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
        this.hospitalRepository = hospitalRepository;
    }

    // API
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

                    Double latitudeDouble = Double.parseDouble(latitude);
                    Double longitudeDouble = Double.parseDouble(longitude);

                    String[] addressParts = address.split(" ");
                    String city = addressParts.length > 0 ? addressParts[0] : "";
                    String district = addressParts.length > 1 ? addressParts[1] : "";

                    // 병원이 이미 존재하는지 확인 (병원 이름 기준으로 중복 체크)
                    boolean exists = hospitalRepository.existsByPhone(phone);
                    if (!exists) {
                        // Hospital 엔티티로 변환하여 DB에 저장
                        HospitalData hospital = new HospitalData(name, address, phone, latitudeDouble, longitudeDouble, city, district);
                        hospitalRepository.save(hospital);  // DB에 저장
                    }

                    // HospitalResponse 객체 추가
                    HospitalResponse hospitalResponse = new HospitalResponse(name, address, phone, latitudeDouble, longitudeDouble, city, district);
                    hospitalList.add(hospitalResponse);
                }
            }

            return hospitalList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // API 값 DB 저장
    public void saveAllHospitals(int numOfRows) throws IOException, URISyntaxException {
        int pageNo = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            // API에서 병원 목록을 가져옴
            List<HospitalResponse> hospitalList = getHospitalList(pageNo, numOfRows);

            if (hospitalList.isEmpty()) {
                break;
            }

            // 병원 목록을 DB에 저장
            for (HospitalResponse hospitalResponse : hospitalList) {
                // 병원이 이미 존재하는지 확인 (병원 이름 기준으로 중복 체크)
                boolean exists = hospitalRepository.existsByPhone(hospitalResponse.getPhone());
                if (!exists) {
                    // Hospital 엔티티로 변환하여 DB에 저장
                    HospitalData hospital = new HospitalData(
                            hospitalResponse.getName(),
                            hospitalResponse.getAddress(),
                            hospitalResponse.getPhone(),
                            hospitalResponse.getLatitude(),
                            hospitalResponse.getLongitude(),
                            hospitalResponse.getCity(),
                            hospitalResponse.getDistrict()
                    );
                    hospitalRepository.save(hospital);
                }
            }

            // 다음 페이지가 있는지 확인
            pageNo++;
            hasNextPage = hospitalList.size() == numOfRows;
        }
    }

    // 모든 병원 목록 조회
    public Page<HospitalResponse> getAllHospitals(int page, int size) {
        Pageable pageable = PageRequest.of(page, 10);
        return hospitalRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    // 지역으로 병원 검색
    public Page<HospitalResponse> searchHospitals(String city, String district, int page, int size) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<HospitalData> hospitalPage;

        if (city != null && district != null) {
            hospitalPage = hospitalRepository.findByCityAndDistrict(city, district, pageable);
        } else if (city != null) {
            hospitalPage = hospitalRepository.findByCity(city, pageable);
        } else if (district != null) {
            hospitalPage = hospitalRepository.findByDistrict(district, pageable);
        } else {
            hospitalPage = hospitalRepository.findAll(pageable);
        }

        return hospitalPage.map(this::convertToResponse);
    }

    // 병원명으로 병원 검색
    public Page<HospitalResponse> searchHospitalsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 병원명에 해당하는 병원 목록 검색
        Page<HospitalData> hospitalPage = hospitalRepository.findByNameContaining(name, pageable);

        return hospitalPage.map(this::convertToResponse);
    }

    private HospitalResponse convertToResponse(HospitalData hospital) {
        return new HospitalResponse(
                hospital.getName(),
                hospital.getPhone(),
                hospital.getAddress(),
                hospital.getLatitude(),
                hospital.getLongitude(),
                hospital.getCity(),
                hospital.getDistrict()
        );
    }
}