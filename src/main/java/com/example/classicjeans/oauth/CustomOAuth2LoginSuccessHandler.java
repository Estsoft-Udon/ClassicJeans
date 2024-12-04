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
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UsersDetailService usersDetailService;  // 사용자 서비스 (DB 조회용)

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // 모든 속성 출력
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
        String providerId = oauthToken.getAuthorizedClientRegistrationId();
        String uniqueKey = null;

        if ("google".equals(providerId)) {
            request.getSession().setAttribute("providerId", "google");
            uniqueKey = "G_" + attributes.get("sub");
        } else if ("kakao".equals(providerId)) {
            request.getSession().setAttribute("providerId", "kakao");
            Long kakaoId = (Long) attributes.get("id");
            uniqueKey = "K_" + kakaoId;
        } else if ("naver".equals(providerId)) {
            request.getSession().setAttribute("providerId", "naver");
            uniqueKey = "N_" + attributes.get("id");
        }

        System.out.println("uniqueKey = " + uniqueKey);
        request.getSession().setAttribute("providerId", uniqueKey);
        request.getSession().setAttribute("uniqueKey", uniqueKey);

        // 이메일을 기준으로 기존 사용자 조회
        UserDetails userDetails = usersDetailService.loadUserByUniqueKey(uniqueKey);

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