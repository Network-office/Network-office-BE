package dev.office.networkoffice.global.config.security;

import dev.office.networkoffice.global.exception.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 사용자가 접근할 때 호출되는 핸들러입니다.
 * 클라이언트에게 401 Unauthorized 응답을 반환합니다.
 * @See org.springframework.http.HttpStatus.UNAUTHORIZED
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExceptionResponse exceptionResponse = ExceptionResponse.of("로그인이 필요합니다.");
        response.getWriter().write(exceptionResponse.toString());

        log.error("Unauthorized Error: {}", authException.getMessage());
    }
}
