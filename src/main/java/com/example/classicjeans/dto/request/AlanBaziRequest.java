package com.example.classicjeans.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlanBaziRequest {
    private LocalDate birthDate;
    private String gender;

    // 사용자 생년월일과 성별을 기반으로 운세 요청 메시지를 생성
    @Override
    public String toString() {
        StringBuilder requestMessage = new StringBuilder();

        // 생년월일과 성별을 기반으로 운세 요청 메시지 구성
        requestMessage.append(birthDate)
                .append(" ")
                .append(gender)
                .append("의 오늘의 운세를 알려줘. ");

        return requestMessage.toString();
    }
}