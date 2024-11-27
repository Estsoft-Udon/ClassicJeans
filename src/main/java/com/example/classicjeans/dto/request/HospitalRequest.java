package com.example.classicjeans.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HospitalRequest {
    private String city;
    private String district;
    private String hospitalName;
}