package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.NursingHomeRequest;
import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.entity.NursingHomeData;
import com.example.classicjeans.repository.NursingHomeDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NursingHomeService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final NursingHomeDataRepository repository;

    public List<NursingHomeData> getNursingHomeDatas() {
        return repository.findAll();
    }

    public void setNursingHomeDatas() throws Exception {
        List<NursingHomeRequest> nursingHomeDatas = parseJsonToDTOList(fetchNursingHomeData());

        for(NursingHomeRequest response : nursingHomeDatas) {
            if(repository.existsByName(response.getName())) {
                continue;
            }

            NursingHomeData nursingHomeData = new NursingHomeData(response);
            String[] addressParts = nursingHomeData.getAddress().split(" ");
            String region = addressParts.length > 0 ? addressParts[0] : "";
            String subRegion = addressParts.length > 1 ? addressParts[1] : "";
            nursingHomeData.setRegion(region);
            nursingHomeData.setSubRegion(subRegion);

            repository.save(nursingHomeData);
        }
    }

    public String fetchNursingHomeData() {
        try {
            String API_KEY = "IHUZ2GEZ-IHUZ-IHUZ-IHUZ-IHUZ2GEZN5";

            String url = "https://safemap.go.kr/openApiService/data/getEWFData.do" +
                    "?serviceKey=" + API_KEY +
                    "&dataType=json" +
                    "&pageNo=1" +
                    "&numOfRows=1000000" +
                    "&Fclty_Cd=513011";

            URI uri = new URI(url);

            ResponseEntity<String> response = restTemplate.exchange(
                    uri, HttpMethod.GET, null, String.class);

            return response.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public List<NursingHomeRequest> parseJsonToDTOList(String json) throws JsonProcessingException {
        if(json == null) {
            return new ArrayList<>();
        }
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode itemsNode = rootNode.path("response").path("body").path("items");
        List<NursingHomeRequest> requests = objectMapper.convertValue(
                itemsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, NursingHomeRequest.class));

        return requests;
    }

    public Page<NursingHomeResponse> getNursingHomeList(Pageable pageable) {
        Page<NursingHomeData> nursingHomeData = repository.findAll(pageable);

        return nursingHomeData.map(NursingHomeResponse::new);
    }

    public Page<NursingHomeResponse> getNursingHomeByRegion(Pageable pageable, String region, String subregion) {
        Page<NursingHomeData> nursingHomeData = repository.findAllByRegionAndSubRegion(region, subregion, pageable);

        return nursingHomeData.map(NursingHomeResponse::new);
    }

    public Page<NursingHomeResponse> searchNursingHomeByName(Pageable pageable, String search) {
        Page<NursingHomeData> nursingHomeData = repository.findAllByNameContaining(search, pageable);

        return nursingHomeData.map(NursingHomeResponse::new);
    }

}