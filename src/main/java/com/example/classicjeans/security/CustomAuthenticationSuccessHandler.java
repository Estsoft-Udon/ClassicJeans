package com.example.classicjeans.security;

import com.example.classicjeans.dto.request.AlanBaziRequest;
import com.example.classicjeans.entity.Users;
import com.example.classicjeans.repository.UsersRepository;
import com.example.classicjeans.service.AlanBaziService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class CustomAuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final AlanBaziService alanBaziService;
    private final UsersRepository usersRepository;

    public CustomAuthenticationSuccessHandler(AlanBaziService alanBaziService, UsersRepository usersRepository) {
        this.alanBaziService = alanBaziService;
        this.usersRepository = usersRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 인증된 사용자 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // 사용자 ID 가져오기
        Users user = usersRepository.findByLoginId(username);

        // AlanBaziRequest 생성 (필요한 값을 request에서 가져오거나 기본값 설정)
        AlanBaziRequest alanBaziRequest = new AlanBaziRequest();

        try {
            // Bazi 저장
            alanBaziService.saveBazi(user.getId(), alanBaziRequest);
        } catch (Exception e) {
            e.printStackTrace();
            // 로그 남기거나 에러 처리 로직 추가 가능
        }

        // 기본 리다이렉트 수행
        response.sendRedirect("/"); // 로그인 후 리다이렉트할 경로 설정
    }
}