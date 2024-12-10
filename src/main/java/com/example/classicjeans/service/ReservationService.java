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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationQueue reservationQueue;
    private final ReservationRepository reservationRepository;
    private final UsersService usersService;
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
        Users user = usersService.findById(request.getUserId());
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
}
