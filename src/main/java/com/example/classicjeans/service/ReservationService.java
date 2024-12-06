package com.example.classicjeans.service;

import com.example.classicjeans.entity.HospitalData;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.HospitalRepository;
import com.example.classicjeans.entity.Reservation;
import com.example.classicjeans.entity.ReservationQueue;
import com.example.classicjeans.repository.ReservationRepository;
import com.example.classicjeans.dto.request.ReservationRequest;
import lombok.RequiredArgsConstructor;
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

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findAllByUserId(Long userId) {
        return reservationRepository.findAllByUserId(userId);
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

    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public void addReservationWhenConstruct() {
        reservationQueue.addReservations(reservationRepository.findAll());
    }

    public int getQueueSize() {
        System.out.println("reservationQueue = " + reservationQueue.getQueueSize());
        return reservationQueue.getQueueSize();
    }

    public String formatString(Reservation reservation) {
        String message = "%s님, %s월 %s일 %s시 %s분,<br>" +
                "%s에 예약이 확정되었습니다! <br>" +
                "잊지 말고 일정에 맞춰 방문해 주세요!😊";
        String reserverName = reservation.getReserverName();;
        LocalDateTime reservationTime = reservation.getTime();
        String month = String.valueOf(reservationTime.getMonthValue());
        String day = String.valueOf(reservationTime.getDayOfMonth());
        String hour = String.valueOf(reservationTime.getHour());
        String minute = String.valueOf(reservationTime.getMinute());
        String place = reservation.getHospital().getName();

        return String.format(message, reserverName, month, day, hour, minute, place);
    }
}
