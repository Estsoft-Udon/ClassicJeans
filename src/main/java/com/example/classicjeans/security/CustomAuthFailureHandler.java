package com.example.classicjeans.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = exception.getMessage();
        } else if (exception instanceof DisabledException) {
            errorMessage = "계정이 비활성화되어 있습니다.";
        } else if (exception instanceof LockedException) {
            errorMessage = "계정이 잠겨 있습니다.";
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "계정의 유효 기간이 만료되었습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "비밀번호의 유효 기간이 만료되었습니다.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";
        }

        // 세션에 에러 메시지 저장
        request.getSession().setAttribute("error", errorMessage);
        setDefaultFailureUrl("/login?error=true");
        super.onAuthenticationFailure(request, response, exception);
    }
}
