package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.ReservationRequest;
import com.example.classicjeans.dto.response.ReservationResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.ReservationNotificationService;
import com.example.classicjeans.service.ReservationService;
import com.example.classicjeans.entity.Reservation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationNotificationService notificationService;
    private final ObjectMapper objectMapper;

    @GetMapping("/reservation/stream")
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter();

        // 구독 처리: userId에 해당하는 emitter를 관리
        if (getLoggedInUser() != null) {
            Long userId = getLoggedInUser().getId();
            emitter = notificationService.subscribe(userId);
        }

        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/reservation")
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest request) {
        if (getLoggedInUser() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reservationService.addReservation(request));
    }

    @GetMapping("/reservation")
    public void sendNotification() throws JsonProcessingException {
        LocalDateTime oneDayAfter = LocalDateTime.now().plusDays(1);

        boolean processing = true;
        while (processing) {
            Reservation reservation = reservationService.getNextReservation();
            if (reservation != null) {
                // 예약시간이 현재 시간의 하루전보다 이전이면 알림 후 알림 여부 true로
                if (reservation.getTime().isBefore(oneDayAfter)) {
                    // 여기서 알림 전송
                    String message = objectMapper.writeValueAsString(reservation);

                    notificationService.sendNotification(reservation.getUser().getId(), message);
                    reservationService.notifyReservation(reservation);
                } else {
                    reservationService.addReservationToQueue(reservation);
                    processing = false; // 종료 조건 설정
                }
            } else {
                processing = false; // null일 때 종료
            }
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservations =
                reservationService.findAll().stream().map(ReservationResponse::new).toList();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Reservation>> getNotifications() {
        Users user = getLoggedInUser();
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Reservation> notifications = reservationService.findAllByUserId(user.getId());

        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reservation/read/{id}")
    public ResponseEntity<Reservation> setReservationRead(@PathVariable Long id) {

        return ResponseEntity.ok(reservationService.setReadTrue(id));
    }

    @DeleteMapping("/reservation/read/{id}")
    public ResponseEntity<Reservation> setReservationUnRead(@PathVariable Long id) {

        return ResponseEntity.ok(reservationService.setReadFalse(id));
    }
}
