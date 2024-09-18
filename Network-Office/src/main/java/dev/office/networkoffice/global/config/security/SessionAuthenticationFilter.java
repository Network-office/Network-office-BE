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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 세션에 저장된 사용자 정보를 기반으로 SecurityContext 내부에 인증 정보(Authentication)를 설정하는 필터입니다.
 * 인증 요청이 아닌 경우, 세션에 저장된 사용자 정보가 유효한 경우에만 인증을 설정합니다.
 * 인증 정보가 이미 설정된 경우, 추가적인 인증을 수행하지 않습니다.
 * 현재는 사용자의 인증 여부에 따라 권한을 부여하고 있습니다. (인증된 사용자: VERIFIED_USER 권한 부여)
 */
@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = Collections.emptyList();
    private static final List<GrantedAuthority> VERIFIED_USER_AUTHORITIES = List.of(new SimpleGrantedAuthority("VERIFIED_USER"));

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        authenticateFromSession(request);
        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    private void authenticateFromSession(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        Long userId = getUserIdFromSession(httpSession);
        if (userId == null) {
            return;
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return;
        }

        Authentication authentication = createAuthentication(optionalUser.get());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Long getUserIdFromSession(HttpSession httpSession) {
        return Optional.ofNullable(httpSession)
                .map(session -> (Long) session.getAttribute("userId"))
                .orElse(null);
    }

    private Authentication createAuthentication(User user) {
        List<GrantedAuthority> authorities = getAuthorities(user);
        return new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        return user.isVerified() ? VERIFIED_USER_AUTHORITIES : DEFAULT_AUTHORITIES;
    }
}
