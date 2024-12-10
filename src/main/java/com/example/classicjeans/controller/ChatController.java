package com.example.classicjeans.controller;


import com.example.classicjeans.service.AlanSSEService;
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

    // SSE 연결 설정
    @GetMapping("/stream")
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter(0L); // 무제한 타임아웃
        String emitterId = String.valueOf(emitter.hashCode());

        System.out.println("SSE 연결 시작 : " + emitterId);
        emitters.put(emitterId, emitter);

        // 클라이언트에 emitterId 전송
        try {
            emitter.send(SseEmitter.event().name("emitterId").data(emitterId));
        } catch (IOException e) {
            emitters.remove(emitterId);
        }

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
                // SSE를 통해 OpenAI API 스트리밍 응답 받기
                alanSSEService.streamChatResponse(content, emitter);

            } catch (IOException e) {
                emitters.remove(id);
            }
        });
    }

    // SSE 연결 종료
    @PostMapping("/stream/close")
    public void closeConnection(@RequestBody String emitterId) {
        SseEmitter emitter = emitters.remove(emitterId);
        System.out.println("SSE 연결 종료 " + emitterId);

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
