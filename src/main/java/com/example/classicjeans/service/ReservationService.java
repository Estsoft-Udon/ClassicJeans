package com.example.classicjeans.service;

import com.example.classicjeans.entity.HospitalData;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.HospitalRepository;
import com.example.classicjeans.entity.Reservation;
import com.example.classicjeans.entity.ReservationQueue;
import com.example.classicjeans.repository.ReservationRepository;
import com.example.classicjeans.dto.request.ReservationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import static com.example.classicjeans.util.SecurityUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationQueue reservationQueue;
    private final ReservationRepository reservationRepository;
    private final HospitalRepository hospitalRepository;
    private final ObjectMapper objectMapper;
    private final ReservationNotificationService notificationService;

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findAllByUserId(Long userId) {
        return reservationRepository.findAllByUserIdAndIsNotificatedTrue(userId);
    }

    // 큐와 DB에 예약 추가
    public void addReservationToQueue(Reservation reservation) {
        reservationQueue.addReservation(reservation);
    }

    public Reservation addReservation(ReservationRequest request) {
        if(getLoggedInUser() == null) {
            return null;
        }
        Users user = getLoggedInUser();
        HospitalData hospital = hospitalRepository.findById(request.getHospitalId())
                .orElse(null);

        Reservation reservation = new Reservation(user, request.getReserverName(), hospital, request.getTime());
        reservationQueue.addReservation(reservation);

        return reservationRepository.save(reservation);
    }

    public Reservation getNextReservation() {
        return reservationQueue.getNextReservation();
    }

    public Reservation notifyReservation(Reservation reservation) {
        reservation.setIsNotificated(true);
        return reservationRepository.save(reservation);
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation toggleReadStatus(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if(reservation == null) {
            return null;
        }

        // 예약의 읽음 상태를 토글
        boolean currentStatus = reservation.getIsRead();
        reservation.setIsRead(!currentStatus);

        return reservationRepository.save(reservation);
    }

    @PostConstruct
    public void addReservationWhenConstruct() {
        reservationQueue.addReservations(reservationRepository.findByIsNotificatedFalse());
    }

    @Scheduled(fixedRate = 60000) // 1분(60,000ms)마다 실행
    public void sendNotificationScheduling() throws JsonProcessingException {
        LocalDateTime oneDayAfter = LocalDateTime.now().plusDays(1);

        boolean processing = true;
        while (processing) {
            Reservation reservation = getNextReservation();
            if (reservation != null) {
                // 예약시간이 현재 시간의 하루전보다 이전이면 알림 후 알림 여부 true로
                if (reservation.getTime().isBefore(oneDayAfter)) {
                    // 여기서 알림 전송
                    String message = objectMapper.writeValueAsString(reservation);

                    notificationService.sendNotification(reservation.getUser().getId(), message);
                    notifyReservation(reservation);
                } else {
                    addReservationToQueue(reservation);
                    processing = false; // 종료 조건 설정
                }
            } else {
                processing = false; // null일 때 종료
            }
        }
    }
}