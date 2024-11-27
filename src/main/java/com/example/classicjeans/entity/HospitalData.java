package com.example.classicjeans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class HospitalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column()
    private Double latitude;

    @Column()
    private Double longitude;

    @Column(nullable = false)
    private String region;

    @Column(name = "sub_region", nullable = false)
    private String subRegion;
}
