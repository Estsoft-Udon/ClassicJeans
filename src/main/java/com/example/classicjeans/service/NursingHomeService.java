package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.NursingHomeRequest;
import com.example.classicjeans.entity.NursingHomeData;
import com.example.classicjeans.repository.NursingHomeDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

            System.out.println(response.getBody());

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
        List<NursingHomeRequest> responses = objectMapper.convertValue(
                itemsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, NursingHomeRequest.class));

        return responses;
    }
}
