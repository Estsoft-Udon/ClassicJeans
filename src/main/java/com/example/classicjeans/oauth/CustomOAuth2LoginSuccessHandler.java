package com.example.classicjeans.oauth;

import com.example.classicjeans.security.UsersDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UsersDetailService usersDetailService;  // 사용자 서비스 (DB 조회용)

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2AuthenticationToken에서 이메일을 가져오기

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String email = oauthToken.getPrincipal().getAttribute("email");  // 'email'은 OAuth2 제공자에 따라 다를 수 있음

        System.out.println(email);
        // 이메일을 기준으로 기존 사용자 조회
        UserDetails userDetails = usersDetailService.loadUserByEmail(email);
        System.out.println(email);

        if (userDetails != null) {
            // 기존 사용자라면 해당 사용자로 로그인 처리
            Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(userAuth);
        } else {
            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/signup")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}