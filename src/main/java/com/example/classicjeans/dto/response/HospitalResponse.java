package com.example.classicjeans.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalResponse {
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;
    private String city;
    private String district;

    public HospitalResponse(String name, String address, String phone, double latitude, double longitude, String city, String district) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.city = city;
    }
}