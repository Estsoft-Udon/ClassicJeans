package com.example.classicjeans.controller.rest;


import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanSSEService;
import com.example.classicjeans.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("api/chat")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final AlanSSEService alanSSEService;

    // SSE 연결 설정
    @GetMapping("/stream")
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter(0L); // 무제한 타임아웃
        Users user = SecurityUtil.getLoggedInUser();
        Long userId = user.getId();

        System.out.println("SSE 연결 시작 연결된 userId: " + userId);
        emitters.put(userId, emitter);

        // 클라이언트에 emitterId 전송
        try {
            emitter.send(SseEmitter.event().name("userId").data(userId));
        } catch (IOException e) {
            emitters.remove(userId);
            return ResponseEntity.status(500).body(null); // 오류 응답
        }

        emitter.onCompletion(() -> {
            emitters.remove(userId);
            System.out.println("SSE 연결 종료 (onCompletion): " + userId);
        });
        emitter.onTimeout(() -> {
            emitters.remove(userId);
            System.out.println("SSE 연결 종료 (onTimeout): " + userId);
        });
        emitter.onError((ex) -> {
            emitters.remove(userId);
            System.out.println("SSE 연결 종료 (onError): " + userId);
        });

        return ResponseEntity.ok(emitter);
    }

    // 메시지 전송 및 브로드캐스트
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody String content) {
        emitters.forEach((id, emitter) -> {
            try {
                // SSE를 통해 OpenAI API 스트리밍 응답 받기
                alanSSEService.streamChatResponse(content, emitter);

            } catch (IOException e) {
                emitters.remove(id);
            }
        });
        return ResponseEntity.ok().build();
    }

    // SSE 연결 종료
    @PostMapping("/stream/close")
    public ResponseEntity<Void> closeConnection(@RequestParam Long userId) {
        SseEmitter emitter = emitters.remove(userId);
        System.out.println("SSE 연결 종료 " + userId);

        if (emitter != null) {
            try {
                System.out.println(" 연결이 종료 되었습니다.");
                emitter.complete();
            } catch (Exception e) {
                // 연결 종료 실패 처리
                emitter.completeWithError(e);
                return ResponseEntity.status(500).build();
            }
        }
        return ResponseEntity.ok().build();
    }
}