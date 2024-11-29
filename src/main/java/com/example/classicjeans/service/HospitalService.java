package com.example.classicjeans.service;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.entity.Hospital;
import com.example.classicjeans.repository.HospitalRepository;
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

    // 전체 병원 목록 DB 저장
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
                    Hospital hospital = new Hospital(
                            hospitalResponse.getName(),
                            hospitalResponse.getAddress(),
                            hospitalResponse.getPhone(),
                            hospitalResponse.getLatitude(),
                            hospitalResponse.getLongitude(),
                            hospitalResponse.getCity(),
                            hospitalResponse.getDistrict()
                    );
                    hospitalRepository.save(hospital);
                } else {
                    System.out.println("이미 존재하는 병원: " + hospitalResponse.getName());
                }
            }

            // 다음 페이지가 있는지 확인
            pageNo++;
            hasNextPage = hospitalList.size() == numOfRows;
        }
    }

    // 병원 목록 조회
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
                        Hospital hospital = new Hospital(name, address, phone, latitudeDouble, longitudeDouble, city, district);
                        hospitalRepository.save(hospital);  // DB에 저장
                    } else {
                        System.out.println("이미 존재하는 병원: " + name);
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

    // 모든 병원 목록 조회
    public List<HospitalResponse> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll(); // DB에서 모든 병원 조회
        List<HospitalResponse> hospitalResponses = new ArrayList<>();

        for (Hospital hospital : hospitals) {
            // Hospital 엔티티를 HospitalResponse DTO로 변환하여 반환
            HospitalResponse hospitalResponse = new HospitalResponse(
                    hospital.getName(),
                    hospital.getPhone(),
                    hospital.getAddress(),
                    hospital.getLatitude(),
                    hospital.getLongitude(),
                    hospital.getCity(),
                    hospital.getDistrict()
            );
            hospitalResponses.add(hospitalResponse);
        }
        return hospitalResponses;
    }


    // city와 district로 병원 검색
    public List<HospitalResponse> searchHospitals(String city, String district) {
        List<Hospital> hospitals;

        if (city != null && district != null) {
            hospitals = hospitalRepository.findByCityAndDistrict(city, district);
        } else if (city != null) {
            hospitals = hospitalRepository.findByCity(city);
        } else if (district != null) {
            hospitals = hospitalRepository.findByDistrict(district);
        } else {
            hospitals = hospitalRepository.findAll(); // city와 district가 없으면 모든 병원 반환
        }

        // Hospital 엔티티를 HospitalResponse DTO로 변환하여 반환
        List<HospitalResponse> hospitalResponses = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            HospitalResponse hospitalResponse = new HospitalResponse(
                    hospital.getName(),
                    hospital.getPhone(),
                    hospital.getAddress(),
                    hospital.getLatitude(),
                    hospital.getLongitude(),
                    hospital.getCity(),
                    hospital.getDistrict()
            );
            hospitalResponses.add(hospitalResponse);
        }

        return hospitalResponses;
    }
}