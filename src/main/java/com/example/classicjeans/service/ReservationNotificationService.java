package com.example.classicjeans.service;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReservationNotificationService {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(0L); // 무제한 시간
        emitters.put(userId, emitter);
        try {
            emitter.send(SseEmitter.event().name("userId").data(userId));
            System.out.println("sse 연결 성공");
        } catch (IOException e) {
            emitters.remove(userId);
            System.out.println("sse 연결 실패");
        }

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));

        return emitter;
    }

    public void unsubscribe(Long userId) {
        emitters.remove(userId);
    }

    public void sendNotification(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(message, MediaType.TEXT_EVENT_STREAM);
                System.out.println(message);
            } catch (IOException e) {
                emitters.remove(userId); // 전송 실패 시 제거
            }
        }
    }
}