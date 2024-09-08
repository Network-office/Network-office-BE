package dev.office.networkoffice.global.config.security;

import dev.office.networkoffice.global.exception.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증된 사용자가 접근 권한이 없는 경우 호출되는 핸들러입니다.
 * 클라이언트에게 403 Forbidden 응답을 반환합니다.
 * @See org.springframework.http.HttpStatus.FORBIDDEN
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExceptionResponse exceptionResponse = ExceptionResponse.of("접근 권한이 없습니다.");
        response.getWriter().write(exceptionResponse.toString());

        log.error("Forbidden Error: {}", accessDeniedException.getMessage());
    }
}
