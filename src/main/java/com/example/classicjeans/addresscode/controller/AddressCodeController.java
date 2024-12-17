package com.example.classicjeans.addresscode.controller;

import com.example.classicjeans.addresscode.service.AddressCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "AddressCode api", description = "주소코드 저장")
@RestController
@RequestMapping("/api/address-code")
@RequiredArgsConstructor
public class AddressCodeController {
    private final AddressCodeService addressCodeService;

    @Operation(summary = "주소코드 저장")
    @PostMapping
    public void saveAddressCode() throws IOException {
        addressCodeService.readCsvAndSave(new ClassPathResource("addresscode.csv").getFile().getPath());
    }
}