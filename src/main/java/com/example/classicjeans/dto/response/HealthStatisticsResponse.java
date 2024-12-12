package com.example.classicjeans.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthStatisticsResponse {
    private Double bmi;
    private Double healthIndex;
    private LocalDate date;
}