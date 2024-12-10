package com.example.classicjeans.controller.view;

import com.example.classicjeans.entity.Bazi;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.service.AlanBaziService;
import com.example.classicjeans.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    
    private final AlanBaziService alanBaziService;
    private final UsersService usersService;

    @RequestMapping("/")
    public String main(Model model) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String loginId = null;

            if (principal instanceof UserDetails) {
                // UserDetails로부터 login_id 가져오기
                UserDetails userDetails = (UserDetails) principal;
                loginId = userDetails.getUsername();  // login_id는 username에 해당한다고 가정
            }

            // login_id로 사용자 정보 조회
            if (loginId != null) {
                Users users = usersService.findByLoginId(loginId);
                if (users != null) {
                    Long userId = users.getId(); // id를 가져옴
                    Bazi bazi = alanBaziService.getMostRecentBaziByUserId(userId); // 가장 최근의 운세 가져오기

                    // 운세 존재 여부 확인
                    if (bazi != null) {
                        model.addAttribute("nickname", users.getNickname());
                    } else {
                        model.addAttribute("nickname", users.getNickname());
                    }
                }
            }
        }

        return "index";
    }

    // 접근 제한
    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
