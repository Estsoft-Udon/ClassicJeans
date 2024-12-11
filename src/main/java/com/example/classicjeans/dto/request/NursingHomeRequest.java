package com.example.classicjeans.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NursingHomeRequest {
    @JsonProperty("FCLTY_NM")
    private String name;

    @JsonProperty("RN_ADRES")
    private String address;

    @JsonProperty("TELNO")
    private String phone;
}