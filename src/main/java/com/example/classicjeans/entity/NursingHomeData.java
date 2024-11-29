package com.example.classicjeans.entity;

import com.example.classicjeans.dto.request.NursingHomeRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class NursingHomeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    public NursingHomeData(NursingHomeRequest request) {
        name = request.getName();
        address = request.getAddress();
        phone = request.getPhone();
    }
}
