package com.example.classicjeans.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {
    private Long userId;
    private String reserverName;
    private Long hospitalId;
    private LocalDateTime time;
}
