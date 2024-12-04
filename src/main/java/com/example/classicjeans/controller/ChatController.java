package com.example.classicjeans.controller;

import static com.example.classicjeans.util.MarkdownRenderer.*;

import com.example.classicjeans.service.AlanSSEService;
import com.example.classicjeans.util.MarkdownRenderer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/chat")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final AlanSSEService alanSSEService;
    private final ObjectMapper objectMapper;

    // SSE 연결 설정
    @GetMapping("/stream")
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter(0L); // 무제한 타임아웃
        String emitterId = String.valueOf(emitter.hashCode());

        emitters.put(emitterId, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(emitterId);
            System.out.println("SSE 연결 종료 (onCompletion): " + emitterId);
        });
        emitter.onTimeout(() -> {
            emitters.remove(emitterId);
            System.out.println("SSE 연결 종료 (onTimeout): " + emitterId);
        });
        emitter.onError((ex) -> {
            emitters.remove(emitterId);
            System.out.println("SSE 연결 종료 (onError): " + emitterId);
        });

        return emitter;
    }

    // 메시지 전송 및 브로드캐스트
    @PostMapping("/send")
    public void sendMessage(@RequestBody String content) {
        emitters.forEach((id, emitter) -> {
            try {
                String response = alanSSEService.getChatResponse(content);

                // json 에서 content 필드 추출
                String extractedContent = objectMapper.readTree(response).get("content").asText();

                // Markdown 언어 rendering
                emitter.send(SseEmitter.event().name("message").data(convertMarkdownToHtml(extractedContent)));
            } catch (IOException e) {
                emitters.remove(id);
            }
        });
    }

    // SSE 연결 종료
    @PostMapping("/stream/close")
    public void closeConnection(@RequestBody String emitterId) {
        SseEmitter emitter = emitters.remove(emitterId);

        if (emitter != null) {
            try {
                System.out.println(" 연결이 종료 되었습니다.");
                emitter.complete();
            } catch (Exception e) {
                // 연결 종료 실패 처리
                emitter.completeWithError(e);
            }
        }
    }
}
