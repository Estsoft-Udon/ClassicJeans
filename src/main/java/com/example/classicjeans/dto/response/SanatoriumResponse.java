package com.example.classicjeans.dto.response;

import com.example.classicjeans.entity.SanatoriumData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SanatoriumResponse {
    private String name;
    private String address;
    private String region;
    private String subRegion;

    public SanatoriumResponse(SanatoriumData data) {
        this.name = data.getName();
        this.address = data.getAddress();
        this.region = data.getRegion();
        this.subRegion = data.getSubRegion();
    }
}
