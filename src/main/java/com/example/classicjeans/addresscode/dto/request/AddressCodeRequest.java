package com.example.classicjeans.addresscode.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressCodeRequest {
    @JsonProperty("adminNm")
    private String name;
    @JsonProperty("siDoCd")
    private String address1;
    @JsonProperty("siGunGuCd")
    private String address2;
}
