package com.example.classicjeans.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HospitalResponse {
    private String name;
    private String address;
    private String phone;
    private String latitude;
    private String longitude;
    private String city;
    private String district;
}
