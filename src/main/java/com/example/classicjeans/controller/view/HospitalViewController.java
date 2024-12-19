package com.example.classicjeans.controller.view;

import com.example.classicjeans.dto.response.HospitalResponse;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.HospitalService;
import com.example.classicjeans.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HospitalViewController {

    private final HospitalService hospitalService;

    // 병원 목록 조회
    @GetMapping("/hospital-list")
    public String hospitalList(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "district", required = false) String district,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model) {
        Page<HospitalResponse> hospitalPage;

        // 검색어가 있을 경우 병원명으로 검색
        if (search != null && !search.isEmpty()) {
            hospitalPage = hospitalService.searchHospitalsByName(search, page, size);
        } else if (city != null || district != null) {
            // city와 district가 있을 경우 검색
            hospitalPage = hospitalService.searchHospitals(city, district, page, size);
        } else {
            hospitalPage = hospitalService.getAllHospitals(page, size);
        }

        if (hospitalPage.getTotalElements() == 0) {
            model.addAttribute("noResults", true);
        }

        // 페이지네이션
        int totalPages = hospitalPage.getTotalPages();
        int pageSize = 5; // 최대 표시할 페이지 버튼 수
        int halfPageSize = pageSize / 2;

        // 현재 페이지 주변에 페이지 버튼을 보여주는 범위 계산
        int startPage = Math.max(0, page - halfPageSize);
        int endPage = Math.min(totalPages - 1, startPage + pageSize - 1);

        // startPage와 endPage 조정 (페이지 범위가 pageSize보다 작을 경우 보정)
        if (endPage - startPage + 1 < pageSize) {
            startPage = Math.max(0, endPage - pageSize + 1);
        }

        model.addAttribute("hospitals", hospitalPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("city", city);
        model.addAttribute("district", district);
        model.addAttribute("size", size);
        model.addAttribute("search", search);

        Users user = SecurityUtil.getLoggedInUser();
        if (user != null) {
            model.addAttribute("loginUser", user);
        }

        return "info/hospital-list";
    }
}