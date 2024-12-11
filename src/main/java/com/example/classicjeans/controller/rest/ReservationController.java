package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.ReservationRequest;
import com.example.classicjeans.dto.response.ReservationResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.ReservationNotificationService;
import com.example.classicjeans.service.ReservationService;
import com.example.classicjeans.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationNotificationService notificationService;

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

    @PatchMapping("/reservation/read/{id}")
    public ResponseEntity<Reservation> toggleReservationReadStatus(@PathVariable Long id) {
        // 예약의 현재 상태를 가져와서 토글
        Reservation reservation = reservationService.toggleReadStatus(id);
        return ResponseEntity.ok(reservation);
    }
}