package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.ReservationRequest;
import com.example.classicjeans.dto.response.ReservationResponse;
import com.example.classicjeans.service.ReservationNotificationService;
import com.example.classicjeans.service.ReservationService;
import com.example.classicjeans.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@RestController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationNotificationService notificationService;

    @PostMapping("/api/reservation")
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest request) {
        if(getLoggedInUser() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reservationService.addReservation(request));
    }

    @GetMapping("/sse/connection")
    public SseEmitter connectToSse() {
        SseEmitter emitter = new SseEmitter();
        // 구독 처리: userId에 해당하는 emitter를 관리
        if(getLoggedInUser() != null) {
            Long userId = getLoggedInUser().getId();
            emitter = notificationService.subscribe(userId);
        }

        return emitter;
    }

    @GetMapping("/api/reservation")
    public void sendNotification() {
        reservationService.addReservationWhenConstruct();
        LocalDateTime oneDayAfter = LocalDateTime.now().plusDays(1);

        boolean processing = true;
        while (processing) {
            Reservation reservation = reservationService.getNextReservation();
            if (reservation != null) {
                // 예약시간이 현재 시간의 하루전보다 이전이면 알림 후 삭제
                if (reservation.getTime().isBefore(oneDayAfter)) {
                    // 여기서 알림 전송
                    String message = reservationService.formatString(reservation);
                    System.out.println("message = " + message);

                    notificationService.sendNotification(reservation.getUser().getId(), message);
                    reservationService.deleteReservation(reservation);
                } else {
                    reservationService.addReservationToQueue(reservation);
                    processing = false; // 종료 조건 설정
                }
            } else {
                processing = false; // null일 때 종료
            }
        }
    }

    @GetMapping("/api/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservations =
                reservationService.findAll().stream().map(ReservationResponse::new).toList();
        return ResponseEntity.ok(reservations);
    }
}
