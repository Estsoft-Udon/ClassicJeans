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

    @GetMapping("/sanatorium-list")
    public String getSanatoriumList(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String province,
                                    @RequestParam(required = false) String district,
                                    @RequestParam(required = false) String search,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanatoriumResponse> sanatoriums;

        if (search != null) {
            sanatoriums = service.searchSanatoriumByName(pageable, search);
        } else if (province != null && district != null) {
            sanatoriums = service.getSanatoriumBySubregion(pageable, province, district);
        } else {
            sanatoriums = service.getSanatoriumList(pageable);
        }

        // 페이지네이션
        int totalPages = sanatoriums.getTotalPages();
        int pageSize = 5;
        int halfPageSize = pageSize / 2;

        int startPage = Math.max(0, page - halfPageSize);
        int endPage = Math.min(totalPages - 1, startPage + pageSize - 1);

        if (endPage - startPage + 1 < pageSize) {
            startPage = Math.max(0, endPage - pageSize + 1);
        }

        model.addAttribute("sanatoriums", sanatoriums);
        model.addAttribute("search", search);
        model.addAttribute("province", province);
        model.addAttribute("district", district);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "info/sanatorium-list";
    }
}