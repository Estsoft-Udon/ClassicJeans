package com.example.classicjeans.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalResponse {
    private String name;
    private String address;
    private String phone;
    private String latitude;
    private String longitude;
    private String city;
    private String district;

    public HospitalResponse(String name, String address, String phone, String latitude, String longitude, String city, String district) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.city = city;
    }
}