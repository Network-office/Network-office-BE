package dev.office.networkoffice.global.config.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * CSRF 보호가 필요한 요청인지 판별하는 클래스입니다.
 * 각 요청이 CSRF 보호가 필요한 요청인지 판별합니다.
 * 원활한 개발을 위해 로그인, 스웨거, H2 콘솔 요청은 CSRF 보호에서 제외합니다.
 */
@Component
public class CsrfRequireMatcher implements RequestMatcher {

    private static final Pattern ALLOWED_METHODS = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private static final String LOGIN_URI = "/api/v1/login/**";
    private static final String CSRF_TOKEN_URI = "/api/v1/csrf";
    private static final String H2_CONSOLE_URI = "/h2-console/**";
    private static final String SWAGGER_UI_URI = "/swagger-ui/**";
    private static final String SWAGGER_RESOURCES_URI = "/swagger-resources/**";
    private static final String API_DOCS_URI = "/v3/api-docs/**";
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean matches(HttpServletRequest request) {
        return requiresCsrfProtection(request);
    }

    private boolean requiresCsrfProtection(HttpServletRequest request) {
        return !isExemptedRequest(request) && !isAllowedMethod(request);
    }

    private boolean isAllowedMethod(HttpServletRequest request) {
        return ALLOWED_METHODS.matcher(request.getMethod()).matches();
    }

    private boolean isExemptedRequest(HttpServletRequest request) {
        return isLoginRequest(request)
                || isCsrfTokenRequest(request)
                || isH2ConsoleRequest(request)
                || isSwaggerRequest(request);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return antPathMatcher.match(LOGIN_URI, request.getRequestURI());
    }

    private boolean isCsrfTokenRequest(HttpServletRequest request) {
        return antPathMatcher.match(CSRF_TOKEN_URI, request.getRequestURI());
    }

    private boolean isH2ConsoleRequest(HttpServletRequest request) {
        return antPathMatcher.match(H2_CONSOLE_URI, request.getRequestURI());
    }

    private boolean isSwaggerRequest(HttpServletRequest request) {
        return isSwaggerRequestUri(request) || isSwaggerRequestReferer(request);
    }

    private boolean isSwaggerRequestUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return antPathMatcher.match(SWAGGER_UI_URI, uri)
                || antPathMatcher.match(SWAGGER_RESOURCES_URI, uri)
                || antPathMatcher.match(API_DOCS_URI, uri);
    }

    private boolean isSwaggerRequestReferer(HttpServletRequest request) {
        return getReferer(request).contains("swagger-ui");
    }

    private String getReferer(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Referer"))
                .orElse("");
    }
}
