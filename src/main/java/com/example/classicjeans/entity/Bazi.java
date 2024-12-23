package com.example.classicjeans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Bazi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String content;

    public Bazi(Users user, LocalDate date, String content) {
        this.user = user;
        this.date = date;
        this.content = content;
    }

    public Bazi() {
    }
}