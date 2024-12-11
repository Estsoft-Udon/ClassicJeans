package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.response.NursingHomeResponse;
import com.example.classicjeans.service.NursingHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NursingHomeViewController {
    private final NursingHomeService service;

    @GetMapping("/nursing-list")
    public String getNursingList(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String province,
                                 @RequestParam(required = false) String district,
                                 @RequestParam(required = false) String search,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NursingHomeResponse> nursinghomes;

        if(search != null) {
            nursinghomes = service.searchNursingHomeByName(pageable, search);
        }
        else if (province != null && district != null) {
            nursinghomes = service.getNursingHomeByRegion(pageable, province, district);
        } else {
            nursinghomes = service.getNursingHomeList(pageable);
        }

        model.addAttribute("nursinghomes", nursinghomes);
        model.addAttribute("search", search);
        model.addAttribute("province", province);
        model.addAttribute("district", district);

        return "/info/nursing-list";
    }

}
