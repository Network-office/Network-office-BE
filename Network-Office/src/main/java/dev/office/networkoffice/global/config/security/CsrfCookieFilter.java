package dev.office.networkoffice.global.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * CSRF 토큰을 쿠키에 저장하는 필터입니다.
 * CSRF 토큰을 쿠키에 저장하여 SPA에서 사용할 수 있도록 합니다.
 * HttpOnly 속성을 추가하지 않은 이유는 Synchronizer Token Pattern을 사용하기 때문입니다.
 * SameSite 속성을 Lax로 설정하여 CSRF 공격을 방지합니다. (더 높은 보안 수준을 원하는 경우 Strict로 설정)
 * Path 속성을 /로 설정하여 모든 범위에서 사용할 수 있도록 합니다.
 * 개발 환경에서 HTTPS 설정하지 않기 때문에 Secure 속성을 추가하지 않았습니다. (향후 HTTPS 설정 시 추가)
 */
@Component
public class CsrfCookieFilter extends OncePerRequestFilter {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String COOKIE_SAMESITE = "Lax";
    private static final String COOKIE_PATH = "/";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        CsrfToken serverCsrfToken = fetchServerCsrfToken(request);
        if (serverCsrfToken != null) {
            if (isClientCsrfTokenAbsent(request)) {
                setCsrfTokenCookie(response, serverCsrfToken.getToken());
            }
        }

        filterChain.doFilter(request, response);
    }

    private CsrfToken fetchServerCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    private boolean isClientCsrfTokenAbsent(HttpServletRequest request) {
        return getCsrfTokenFromCookie(request) == null;
    }

    private String getCsrfTokenFromCookie(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Optional.of(cookies)
                        .flatMap(this::findCookie)
                        .map(Cookie::getValue))
                .orElse(null);
    }

    private Optional<Cookie> findCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (CSRF_COOKIE_NAME.equals(cookie.getName())) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }

    private void setCsrfTokenCookie(HttpServletResponse response, String csrfToken) {
        String cookieValue = "XSRF-TOKEN=%s; SameSite=%s; Path=%s".formatted(csrfToken, COOKIE_SAMESITE, COOKIE_PATH);
        response.setHeader("Set-Cookie", cookieValue);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    }
}
