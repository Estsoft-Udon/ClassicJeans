package com.example.classicjeans.dto.request;

import lombok.Data;

@Data
public class AlanHealthRequest {

    private int age;
    // 체중
    private int weight;
    // 키
    private int height;

    private boolean smoker;

    private boolean drunken;

    // 현재 앓고 있는 만성 질환
    private String sick;


    @Override
    public String toString() {
        return
                "age=" + age +
                        ", weight=" + weight +
                        ", height=" + height +
                        ", smoker=" + smoker +
                        ", drunken=" + drunken +
                        ", 내가 앓고 있는 만성 질환=" + sick +
                        " 내 건강 문진표야 값을 각 항목 별로 한국 평균 값과 비교 해서 알려줘"
                        + "출처 등 그 어떤 다른 설명 없이 비교만 해줘";
    }
}
