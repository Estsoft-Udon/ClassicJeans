package com.example.classicjeans.entity;

import com.example.classicjeans.dto.request.SanatoriumRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SanatoriumData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String state;

    @Column
    private String city;

    public SanatoriumData(SanatoriumRequest request) {
        name = request.getName();
        address = request.getAddress();
        state = request.getState();
        city = request.getCity();
    }
}
