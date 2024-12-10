package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.response.SanatoriumResponse;
import com.example.classicjeans.service.SanatoriumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class SanatoriumViewController {
    private final SanatoriumService service;

    @GetMapping("/sanatorium_list")
    public String getSanatoriumList(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String province,
                                    @RequestParam(required = false) String district,
                                    @RequestParam(required = false) String search,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanatoriumResponse> sanatoriums;

        if(search != null) {
            sanatoriums = service.searchSanatoriumByName(pageable, search);
        }
        else if (province != null && district != null) {
            sanatoriums = service.getSanatoriumBySubregion(pageable, province, district);
        } else {
            sanatoriums = service.getSanatoriumList(pageable);
        }

        model.addAttribute("sanatoriums", sanatoriums);
        model.addAttribute("search", search);
        model.addAttribute("province", province);
        model.addAttribute("district", district);

        return "/info/sanatorium_list";
    }
}
