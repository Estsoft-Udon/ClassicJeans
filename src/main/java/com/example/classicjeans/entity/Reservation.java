package com.example.classicjeans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;  // 사용자 엔티티와 연결

    @Column
    private String reserverName;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalData hospital;  // 병원 엔티티와 연결

    @Column(name = "time", nullable = false)
    private LocalDateTime time;  // 예약 시간

    @Column(name = "is_notificated")
    private Boolean isNotificated = false;  // 알림 여부

    @Column(name = "is_read")
    private Boolean isRead = false;  // 읽음 여부

    public Reservation(Users user, String reserverName, HospitalData hospital, LocalDateTime time) {
        this.user = user;
        this.reserverName = reserverName;
        this.hospital = hospital;
        this.time = time;
    }
}