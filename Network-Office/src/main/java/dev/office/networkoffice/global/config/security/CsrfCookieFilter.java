package dev.office.networkoffice.global.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * CSRF 토큰을 쿠키에 저장하는 필터입니다.
 * CSRF 토큰을 쿠키에 저장하여 SPA에서 사용할 수 있도록 합니다.
 * 클라이언트에게 CSRF 토큰이 없는 경우에 CSRF 토큰을 쿠키에 저장합니다.
 */
@Component
public class CsrfCookieFilter extends OncePerRequestFilter {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_TOKEN_ATTRIBUTE = "_csrf";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CSRF_TOKEN_ATTRIBUTE);
        if (isClientCsrfTokenAbsent(request)) {
            csrfToken.getToken(); // 지연된 쿠키를 로드하여 쿠키에 CSRF 토큰을 저장합니다.
        }

        filterChain.doFilter(request, response);
    }

    private boolean isClientCsrfTokenAbsent(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> CSRF_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .isEmpty();
    }
}
