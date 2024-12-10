package com.example.classicjeans.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthReportResponse {
    private String type;
    private Long id;
    private String name;
    private String date;
}