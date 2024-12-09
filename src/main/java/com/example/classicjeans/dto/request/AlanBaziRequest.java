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

        // 연관된 내용이 아니라면 다른 띠의 내용은 필요없어라는 메시지 추가
        requestMessage.append("연관된 내용이 아니라면 다른 띠의 내용말고 나의 띠에 대해서만 이야기 해줘. ");
        requestMessage.append("헤럴드 운세 내용만 가져와줘.");
        requestMessage.append("띠별 운세 내용은 나의 띠에 대해서만 이야기 해줘. 띠 잘 모르면 이야기 하지 말아줘.");
        requestMessage.append("마지막으로 한 줄로 요약해줘.");


        return requestMessage.toString();
    }
}