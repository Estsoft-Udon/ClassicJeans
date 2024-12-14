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

        // 페이지네이션
        int totalPages = nursinghomes.getTotalPages();
        int pageSize = 5;
        int halfPageSize = pageSize / 2;

        int startPage = Math.max(0, page - halfPageSize);
        int endPage = Math.min(totalPages - 1, startPage + pageSize - 1);

        if (endPage - startPage + 1 < pageSize) {
            startPage = Math.max(0, endPage - pageSize + 1);
        }

        model.addAttribute("nursinghomes", nursinghomes);
        model.addAttribute("search", search);
        model.addAttribute("province", province);
        model.addAttribute("district", district);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "info/nursing-list";
    }
}