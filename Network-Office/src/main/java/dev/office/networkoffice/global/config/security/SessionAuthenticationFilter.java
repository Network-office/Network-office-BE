package dev.office.networkoffice.global.config.security;

import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 세션에 저장된 사용자 정보를 기반으로 SecurityContext 내부에 인증 정보를 설정하는 필터입니다.
 * 이미 인증된 사용자인 경우 필터를 거치지 않습니다.
 * 세션에 사용자 정보가 없거나 사용자 정보가 유효하지 않은 경우 인증 정보를 설정하지 않습니다.
 * 참고: 현재는 휴대폰 인증을 완료한 사용자인 경우 VERIFIED_USER 권한을 부여합니다.
 */
@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = Collections.emptyList();
    private static final List<GrantedAuthority> VERIFIED_USER_AUTHORITIES = List.of(new SimpleGrantedAuthority("VERIFIED_USER"));

    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (isAuthenticated(context)) {
            filterChain.doFilter(request, response);
            return;
        }

        authenticateFromSession(context);

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticated(SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        return authentication != null;
    }

    private void authenticateFromSession(SecurityContext context) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return;
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return;
        }

        Authentication authentication = createAuthentication(optionalUser.get());
        context.setAuthentication(authentication);
    }

    private Authentication createAuthentication(User user) {
        List<GrantedAuthority> authorities = user.isVerified() ? VERIFIED_USER_AUTHORITIES : DEFAULT_AUTHORITIES;
        return new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);
    }
}
