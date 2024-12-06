package com.example.classicjeans.entity;


import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Component
public class ReservationQueue {

    // 예약시간을 기준으로 알림을 우선순위 큐에 저장
    private final PriorityQueue<Reservation> reservationQueue;

    public ReservationQueue() {
        // 예약시간이 빠른 순으로 정렬되는 큐
        this.reservationQueue = new PriorityQueue<>(Comparator.comparing(Reservation::getTime));
    }

    // 예약 정보를 큐에 추가
    public void addReservation(Reservation reservation) {
        reservationQueue.offer(reservation);
    }

    public void addReservations(List<Reservation> reservations) {
        reservationQueue.addAll(reservations);
    }

    // 예약된 알림을 처리 (가장 우선순위가 높은 알림을 반환)
    public Reservation getNextReservation() {
        return reservationQueue.poll(); // 예약된 알림을 하나 꺼냄
    }

    // 큐에 있는 알림의 크기 반환
    public int getQueueSize() {
        return reservationQueue.size();
    }
}