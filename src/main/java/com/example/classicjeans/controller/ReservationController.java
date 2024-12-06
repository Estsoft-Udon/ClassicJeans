package com.example.classicjeans.controller;

import com.example.classicjeans.dto.request.ReservationRequest;
import com.example.classicjeans.dto.response.ReservationResponse;
import com.example.classicjeans.service.ReservationService;
import com.example.classicjeans.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import static com.example.classicjeans.util.SecurityUtil.*;

@RestController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/api/reservation")
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest request) {
        request.setUserId(getLoggedInUser().getId());

        return ResponseEntity.ok(reservationService.addReservation(request));
    }

    @GetMapping("/api/reservation")
    public void sendNotification() {
        reservationService.addReservationWhenConstruct();
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        boolean processing = true;
        while (processing) {
            Reservation reservation = reservationService.getNextReservation();
            if (reservation != null) {
                System.out.println("reservation.getTime() = " + reservation.getTime());
                System.out.println("oneDayAgo = " + oneDayAgo);

                // 예약시간이 현재 시간의 하루전보다 늦으면 삭제
                if (reservation.getTime().isBefore(oneDayAgo)) {
                    // 여기서 알림 호출

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
