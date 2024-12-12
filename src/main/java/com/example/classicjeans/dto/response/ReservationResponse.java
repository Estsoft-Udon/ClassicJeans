package com.example.classicjeans.dto.response;

import com.example.classicjeans.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResponse {
    private String userLoginId;
    private String reserverName;
    private String hospitalName;
    private LocalDateTime time;

    public ReservationResponse(Reservation reservation) {
        this.userLoginId = reservation.getUser().getLoginId();
        this.reserverName = reservation.getReserverName();
        this.hospitalName = reservation.getHospital().getName();
        this.time = reservation.getTime();
    }
}