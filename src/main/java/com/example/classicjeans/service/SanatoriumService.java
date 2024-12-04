package com.example.classicjeans.service;

import com.example.classicjeans.addresscode.dto.request.AddressCodeRequest;
import com.example.classicjeans.addresscode.service.AddressCodeService;
import com.example.classicjeans.dto.request.SanatoriumRequest;
import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.dto.response.SanatoriumResponse;
import com.example.classicjeans.entity.NursingHomeData;
import com.example.classicjeans.entity.SanatoriumData;
import com.example.classicjeans.repository.SanatoriumDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SanatoriumService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final SanatoriumDataRepository repository;
    private final AddressCodeService addressCodeService;
    private final String[] siDoCodes = {"11", "21", "22", "23", "24", "25", "26", "36", "41", "42", "43", "44", "45", "46", "47", "48", "50"};
    private final String API_KEY = "2ogCXo2SHKelqGs5eqM%2FG%2FLLmJ%2FwJmM%2BABW7tKBCTi1GjRUV7enps2byfYp1KGJXSvou9b2zSxMh%2FVbFZYrn2g%3D%3D";

    public List<SanatoriumData> getSanatoriumDatas() {
        return repository.findAll();
    }

    public void setSanatoriumDatas() throws Exception {
        for(String sidoCode : siDoCodes) {
            List<SanatoriumRequest> datas = new ArrayList<>();
            int totalCount = Math.min(calculateTotalCount(sidoCode), 1000);
            datas.addAll(parseJsonToDTOList(fetchSanatoriumData(sidoCode, totalCount)));
            List<SanatoriumData> sanatoriumDatas = new ArrayList<>();

            for(SanatoriumRequest response : datas) {
                if(repository.existsByName(response.getName())) {
                    continue;
                }

                sanatoriumDatas.add(new SanatoriumData(response));
            }
            repository.saveAll(sanatoriumDatas);
        }
    }

    public String fetchSanatoriumData(String siDoCd, int numOfRows) {
        try {
            String url = "http://apis.data.go.kr/B550928/searchLtcInsttService01/getLtcInsttSeachList01" +
                    "?serviceKey=" + API_KEY +
                    "&numOfRows=" + numOfRows +
                    "&siDoCd=" + siDoCd;

            URI uri = new URI(url);

            ResponseEntity<String> response = restTemplate.exchange(
                    uri, HttpMethod.GET, null, String.class);

            return response.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public List<SanatoriumRequest> parseJsonToDTOList(String json) throws JsonProcessingException {
        if (json == null) {
            return new ArrayList<>();
        }
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
        List<AddressCodeRequest> requests = objectMapper.convertValue(
                itemsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, AddressCodeRequest.class));

        return convertToSanatoriumRequests(requests);
    }

    private List<SanatoriumRequest> convertToSanatoriumRequests(List<AddressCodeRequest> requests) {
        if(requests == null) {
            return new ArrayList<>();
        }

        List<SanatoriumRequest> sanatoriumRequests = new ArrayList<>();

        for (AddressCodeRequest request : requests) {
            SanatoriumRequest sanatoriumRequest = mapToSanatoriumRequest(request);
            String address = sanatoriumRequest.getAddress();
            sanatoriumRequest.setAddress(addressCodeService.getAddress(address));

            String[] addressParts = sanatoriumRequest.getAddress().split(" ");

            switch(addressParts.length) {
                case 3:
                    sanatoriumRequest.setSubRegion(addressParts[1] + " " + addressParts[2]);
                    break;
                case 2:
                    sanatoriumRequest.setSubRegion(addressParts[1]);
                    break;
            }

            if(addressParts.length > 0) {
                sanatoriumRequest.setRegion(addressParts[0]);
            }

            sanatoriumRequests.add(sanatoriumRequest);
        }
        return sanatoriumRequests;
    }

    public SanatoriumRequest mapToSanatoriumRequest(AddressCodeRequest request) {
        return new SanatoriumRequest(
                request.getName(),
                request.getAddress1(),
                request.getAddress2()
        );
    }

    public int calculateTotalCount(String siDoCd) throws JsonProcessingException, URISyntaxException {
        String url = "http://apis.data.go.kr/B550928/searchLtcInsttService01/getLtcInsttSeachList01" +
                "?serviceKey=" + API_KEY +
                "&numOfRows=1" +
                "&siDoCd=" + siDoCd;

        URI uri = new URI(url);

        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.GET, null, String.class);

        JsonNode rootNode = objectMapper.readTree(response.getBody());

        return rootNode.path("response").path("body").path("totalCount").asInt();
    }

    public Page<SanatoriumResponse> getSanatoriumList(Pageable pageable) {
        Page<SanatoriumData> sanatoriumData = repository.findAll(pageable);

        return sanatoriumData.map(SanatoriumResponse::new);
    }

    public Page<SanatoriumResponse> getSanatoriumBySubregion(Pageable pageable, String region, String subregion) {
        Page<SanatoriumData> sanatoriumData = repository.findAllByRegionAndSubRegion(region, subregion, pageable);

        return sanatoriumData.map(SanatoriumResponse::new);
    }

    public Page<SanatoriumResponse> searchSanatoriumByName(Pageable pageable, String search) {
        Page<SanatoriumData> sanatoriumData = repository.findAllByNameContaining(search, pageable);

        return sanatoriumData.map(SanatoriumResponse::new);
    }
}
