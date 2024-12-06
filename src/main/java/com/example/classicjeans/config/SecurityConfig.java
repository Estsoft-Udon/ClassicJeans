package com.example.classicjeans.config;

import com.example.classicjeans.oauth.CustomOAuth2UserService;
import com.example.classicjeans.security.CustomAuthFailureHandler;
import com.example.classicjeans.security.CustomAuthenticationSuccessHandler;
import com.example.classicjeans.security.UsersDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final UsersDetailService usersDetailService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


    @Bean
    public WebSecurityCustomizer ignore() {
        return WebSecurity -> WebSecurity.ignoring()
                .requestMatchers("/css/**", "/js/**", "/img/**", "/error"); // 정적 리소스 허용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
//                        custom -> custom.requestMatchers("/**").permitAll()
//                )
//                .formLogin(custom -> {
//                    custom.loginPage("/login")
//                            .failureHandler(customAuthFailureHandler);
//                })
////                .oauth2Login(oauth2 ->
////                        oauth2.loginPage("/login") // OAuth2 버튼이 포함된 페이지
////                                .defaultSuccessUrl("/") // OAuth2 성공 시 이동
////                                .successHandler(new CustomOAuth2LoginSuccessHandler(usersDetailService))  // 로그인 후 처리할 핸들러 등록
////                                .userInfoEndpoint(userInfo ->
////                                        userInfo.userService(customOAuth2UserService))
////                )
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();

                        custom ->
                                custom
                                        .requestMatchers("/api/alan/bazi").authenticated() // 인증 필요
                                        .anyRequest().permitAll() // 나머지는 허용
                )
                .formLogin(custom -> {
                    custom.loginPage("/login")
                            .defaultSuccessUrl("/", true) // 로그인 성공 시 이동 경로
                            .successHandler(customAuthenticationSuccessHandler)
                            .failureHandler(customAuthFailureHandler);
                })
                .logout(custom -> custom.logoutUrl("/logout").logoutSuccessUrl("/"))
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}