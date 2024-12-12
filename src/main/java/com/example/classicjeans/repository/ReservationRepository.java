package com.example.classicjeans.repository;

import com.example.classicjeans.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 알림이 true인 예약
    List<Reservation> findAllByUserIdAndIsNotificatedTrue(Long userId);
    // 알림과 읽음이 true인 예약
    List<Reservation> findAllByUserIdAndIsNotificatedTrueAndIsReadTrue(Long userId);
    // 알림 true, 읽음 false 예약
    List<Reservation> findAllByUserIdAndIsNotificatedTrueAndIsReadFalse(Long userId);
    List<Reservation> findByIsNotificatedFalse();
}