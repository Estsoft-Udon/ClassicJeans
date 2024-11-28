package com.example.classicjeans.dto.response;

import com.example.classicjeans.entity.NursingHomeData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NursingHomeResponse {
    private String name;
    private String address;
    private String phone;

    public NursingHomeResponse(NursingHomeData data) {
        this.name = data.getName();
        this.address = data.getAddress();
        this.phone = data.getPhone();
    }
}
