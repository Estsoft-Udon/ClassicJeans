package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.dto.response.AlanBaziResponse;
import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.AlanBaziRepository;
import com.example.classicjeans.repository.UsersRepository;
import com.example.classicjeans.util.MarkdownRenderer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.classicjeans.util.RegexPatterns.URL_PATTERN;

@Service
public class AlanBaziService {

    @Value("${CLIENT_ID_5}")
    private static String CLIENT_ID_5;

    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";
    private static final String DELETE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/reset-state";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final AlanBaziRepository alanBaziRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public AlanBaziService(RestTemplateBuilder restTemplate, ObjectMapper objectMapper,
                           AlanBaziRepository alanBaziRepository, UsersRepository usersRepository) {
        this.restTemplate = restTemplate.build();
        this.objectMapper = objectMapper;
        this.alanBaziRepository = alanBaziRepository;
        this.usersRepository = usersRepository;
    }

    // 오늘의 운세
    public AlanBaziResponse fetchBazi(AlanBaziRequest request) {
        try {
            // 기존 로직 수행
            String uri = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL)
                    .queryParam("content", request)
                    .queryParam("client_id", CLIENT_ID_5)
                    .toUriString();

            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            String responseBody = response.getBody();

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode contentNode = rootNode.path("content");

            if (contentNode.isTextual()) {
                String cleanedContent = removeBaziContent(contentNode.asText());
                ((ObjectNode) rootNode).put("content", cleanedContent);
            }

            return objectMapper.treeToValue(rootNode, AlanBaziResponse.class);
        } catch (Exception e) {
            // 앨런 클라이언트 ID 횟수 초기화
            // resetPreviousData();
            // 콘솔에 에러 로그 출력
            e.printStackTrace();  // 콘솔에 전체 스택 트레이스를 출력
            throw new RuntimeException("Error occurred while fetching Bazi", e);  // 클라이언트에 전달할 사용자 정의 메시지
        }
    }

    public Bazi saveBazi(Long userId, AlanBaziRequest request) throws JsonProcessingException {
        AlanBaziResponse response = fetchBazi(request);  // fetchBazi 호출

        Bazi bazi = new Bazi();
        bazi.setUser(usersRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found")));
        bazi.setDate(LocalDate.now()); // 오늘 날짜 저장
        bazi.setContent(response.getContent()); // 운세 내용 저장

        return alanBaziRepository.save(bazi);
    }

    // userId에 해당하는 가장 최근 Bazi 레코드를 가져오는 메서드
    public Bazi getMostRecentBaziByUserId(Long userId) {
        // AlanBaziRepository 인스턴스를 사용하여 메서드를 호출
        List<Bazi> baziList = alanBaziRepository.findByUserId(userId);

        // 오늘 날짜에 해당하는 Bazi 레코드를 필터링하고 가장 최근 날짜의 레코드를 선택
        Optional<Bazi> recentBazi = baziList.stream()
                .filter(bazi -> bazi.getDate().equals(LocalDate.now())) // 오늘 날짜 필터링
                .max(Comparator.comparing(Bazi::getDate)); // 날짜가 최신인 레코드 찾기

        return recentBazi.orElse(null);  // 해당하는 레코드가 없으면 null 반환
    }

    public Boolean GetExistsByUserAndDate(Users user, LocalDate date) {
        return alanBaziRepository.existsByUserAndDate(user, date);
    }

    public AlanBaziResponse getOrCreateBazi(Long userId, AlanBaziRequest request) throws JsonProcessingException {
        // DB에서 오늘 날짜의 운세가 있는지 확인
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 이미 오늘 날짜의 운세가 존재하면 가져오기
        if (GetExistsByUserAndDate(user, LocalDate.now())) {
            Bazi existingBazi = getMostRecentBaziByUserId(userId);
            AlanBaziResponse response = new AlanBaziResponse();

            // 기존 운세의 내용을 정리 (필요한 내용을 제거)
            String cleanedContent = removeBaziContent(existingBazi.getContent());
            String html = MarkdownRenderer.convertMarkdownToHtml(cleanedContent);

            response.setContent(html);
            return response;
        }

        // 오늘 날짜의 운세가 없으면 새 운세 생성 및 저장
        AlanBaziResponse newResponse = fetchBazi(request);

        // 새로 받아온 운세 내용도 정리
        String cleanedContent = removeBaziContent(newResponse.getContent());
        String html = MarkdownRenderer.convertMarkdownToHtml(cleanedContent);
        newResponse.setContent(html);

        // DB에 새로운 운세 저장
        saveBazi(userId, request);
        return newResponse;
    }

    private String removeBaziContent(String text) {
        return text
                .replaceAll(URL_PATTERN, "")
                .replaceAll("\\d{4}년 \\d{1,2}월 \\d{1,2}일생 (남성|여성)\\d의", "") // "0000년 00월 00일에 태어난 남성/여성" 제거
                .replaceAll("\\[.*?]\\(.*?\\)", "") // 마크다운 링크 제거
                .replaceAll("\\d{4}년생은\\s*", "")
                .replaceAll("이 외에도.*", "")
                .replaceAll("다른 띠의 내용은 포함하지 않았습니다\\.?", "")
                .replaceAll("\\*\\*[^*]+\\*\\*에 따르면:", "") // "**XX**에 따르면:" 패턴 제거
                .replaceAll(":\\s*:\\s*:", "") // ': : :' 제거
                .replaceAll("\\*\\*\\*\\*:\\s*-\\s*", "")
                .trim();
    }

    // 메소드 실행 전에 이전 데이터를 초기화
    private void resetPreviousData() {
        String uri = UriComponentsBuilder
                .fromHttpUrl(DELETE_URL)
                .toUriString();

        String jsonBody = "{\"client_id\":\"" + CLIENT_ID_5 + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        } catch (HttpStatusCodeException e) {
            System.err.println("Error during reset: " + e.getResponseBodyAsString());
        }
    }
}