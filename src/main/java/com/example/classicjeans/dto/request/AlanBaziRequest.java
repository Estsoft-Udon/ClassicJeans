package com.example.classicjeans.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlanBaziRequest {
    private LocalDate birthDate;
    private String gender;

    @Override
    public String toString() {
        return birthDate + " " + gender + " " + "의 오늘의 운세를 알려줘. 연관된 내용이 아니라면 다른 띠의 내용은 필요없어.";
    }
}
