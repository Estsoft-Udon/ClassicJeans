package com.example.classicjeans.controller.rest;

import com.example.classicjeans.dto.request.ReservationRequest;
import com.example.classicjeans.dto.response.ReservationResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.ReservationNotificationService;
import com.example.classicjeans.service.ReservationService;
import com.example.classicjeans.entity.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Tag(name = "병원 예약/알림 api", description = "병원 예약하기/조회/알림/삭제/읽음 상태")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationNotificationService notificationService;

    @Operation(summary = "예약 SSE 연결")
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

    @Operation(summary = "예약하기")
    @PostMapping("/reservation")
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest request) {
        if (getLoggedInUser() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reservationService.addReservation(request));
    }

    @Operation(summary = "예약 목록 조회")
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservations =
                reservationService.findAll().stream().map(ReservationResponse::new).toList();
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "예약 알림 목록 조회")
    @GetMapping("/notifications")
    public ResponseEntity<List<Reservation>> getNotifications() {
        Users user = getLoggedInUser();
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Reservation> notifications = reservationService.findAllByUserId(user.getId());

        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "예약 삭제")
    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "읽음 상태 토글")
    @PatchMapping("/reservation/read/{id}")
    public ResponseEntity<Reservation> toggleReservationReadStatus(@PathVariable Long id) {
        // 예약의 현재 상태를 가져와서 토글
        Reservation reservation = reservationService.toggleReadStatus(id);
        return ResponseEntity.ok(reservation);
    }
}