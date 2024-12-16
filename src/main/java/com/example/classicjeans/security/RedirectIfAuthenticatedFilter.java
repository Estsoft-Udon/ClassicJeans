package com.example.classicjeans.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RedirectIfAuthenticatedFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 인증된 상태이고, 요청 URI가 /login 또는 /signup인 경우
        if (authentication != null && authentication.isAuthenticated() &&
                ("/login".equals(request.getRequestURI()) || "/signup".equals(request.getRequestURI()))) {
            // 메인 페이지로 리다이렉트
            response.sendRedirect("/");
            return;
        }

        // 그 외 요청은 필터 체인을 통과
        filterChain.doFilter(request, response);
    }
}