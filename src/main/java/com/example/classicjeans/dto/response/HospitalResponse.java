package com.example.classicjeans.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;
    private String city;
    private String district;

    public HospitalResponse(String name, String phone, String address, Double latitude, Double longitude, String city, String district) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude != null ? latitude : 0.0;
        this.longitude = longitude != null ? longitude : 0.0;
        this.city = city;
        this.district = district;
    }
}