package com.example.classicjeans.service;

import com.example.classicjeans.addresscode.dto.request.AddressCodeRequest;
import com.example.classicjeans.addresscode.service.AddressCodeService;
import com.example.classicjeans.dto.request.SanatoriumRequest;
import com.example.classicjeans.entity.SanatoriumData;
import com.example.classicjeans.repository.SanatoriumDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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


    public List<SanatoriumData> getSanatoriumDatas() {
        return repository.findAll();
    }

    @Transactional(timeout = 3600)
    public void setSanatoriumDatas() throws Exception {
        for(String sidoCode : siDoCodes) {
            List<SanatoriumRequest> datas = new ArrayList<>();
            datas.addAll(parseJsonToDTOList(fetchSanatoriumData(sidoCode)));

            for(SanatoriumRequest response : datas) {
                if(repository.existsByName(response.getName())) {
                    continue;
                }

                SanatoriumData sanatoriumData = new SanatoriumData(response);
                repository.save(sanatoriumData);
            }
        }
    }

    public String fetchSanatoriumData(String siDoCd) {
        try {
            String API_KEY = "2ogCXo2SHKelqGs5eqM%2FG%2FLLmJ%2FwJmM%2BABW7tKBCTi1GjRUV7enps2byfYp1KGJXSvou9b2zSxMh%2FVbFZYrn2g%3D%3D";

            String url = "http://apis.data.go.kr/B550928/searchLtcInsttService01/getLtcInsttSeachList01" +
                    "?serviceKey=" + API_KEY +
                    "&numOfRows=100" +
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
                    sanatoriumRequest.setCity(addressParts[1] + " " + addressParts[2]);
                    break;
                case 2:
                    sanatoriumRequest.setCity(addressParts[1]);
                    break;
            }

            if(addressParts.length > 0) {
                sanatoriumRequest.setState(addressParts[0]);
            }

            sanatoriumRequests.add(sanatoriumRequest);
        }
        return sanatoriumRequests;
    }

    private SanatoriumRequest mapToSanatoriumRequest(AddressCodeRequest request) {
        return new SanatoriumRequest(
                request.getName(),
                request.getAddress1(),
                request.getAddress2()
        );
    }
}
