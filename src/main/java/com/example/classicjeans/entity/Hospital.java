package com.example.classicjeans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hospital_data")
@Getter
@Setter
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private Double latitude;

    private Double longitude;

    private String phone;

    @Column(name = "region")
    private String city;

    @Column(name = "sub_region")
    private String district;


    public Hospital() {}

    public Hospital(String name, String address, String phone, Double latitude, Double longitude, String city, String district) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.district = district;
    }
}
