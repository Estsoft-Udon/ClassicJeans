package com.example.classicjeans.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SanatoriumRequest {
    private String name;
    private String address;
    private String region;
    private String subRegion;

    public SanatoriumRequest(String name, String address1, String address2) {
        this.name = name;
        address = String.format("%s%s00000", address1, address2);
    }
}
