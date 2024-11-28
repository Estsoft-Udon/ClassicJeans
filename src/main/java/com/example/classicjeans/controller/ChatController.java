package com.example.classicjeans.controller;

import com.example.classicjeans.service.AlanSSEService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ChatController {

    private final AlanSSEService alanSSEService;

    @Autowired
    public ChatController(AlanSSEService alanSSEService) {
        this.alanSSEService = alanSSEService;
    }

    // SSE 스트리밍을 처리하는 엔드포인트
    @GetMapping("/chat/stream")
    public SseEmitter getChatStream(@RequestParam String content) {
        return alanSSEService.getChatResponse(content);
    }

    // 클라이언트로부터 메시지를 받아 처리하고 SSE를 통해 응답을 보내는 엔드포인트
    @PostMapping("/chat/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        alanSSEService.getChatResponse(content);  // 메시지를 처리하고 SSE를 통해 응답 전송
        return ResponseEntity.ok().build();
    }

    // 클라이언트가 연결되어 있을 때 메시지를 받는 엔드포인트
    @GetMapping("/chat/receive")
    public SseEmitter receiveMessages() {
        return alanSSEService.getChatResponse("채팅 메시지를 기다리는 중...");
    }
}
