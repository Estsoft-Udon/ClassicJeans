package com.example.classicjeans.addresscode.controller;

import com.example.classicjeans.addresscode.service.AddressCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/address-code")
@RequiredArgsConstructor
public class AddressCodeController {
    private final AddressCodeService addressCodeService;

    @PostMapping
    public void saveAddressCode() throws IOException {
        addressCodeService.readCsvAndSave(new ClassPathResource("addresscode.csv").getFile().getPath());
    }
}