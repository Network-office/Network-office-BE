package dev.office.networkoffice.global.config;

import java.util.List;

import dev.office.networkoffice.global.config.security.CsrfCookieFilter;
import dev.office.networkoffice.global.config.security.CsrfRequireMatcher;
import dev.office.networkoffice.global.config.security.CustomAccessDeniedHandler;
import dev.office.networkoffice.global.config.security.CustomAuthenticationEntryPoint;
import dev.office.networkoffice.global.config.security.SessionAuthenticationFilter;
import dev.office.networkoffice.global.config.security.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SessionAuthenticationFilter sessionAuthenticationFilter;
    private final CsrfCookieFilter csrfCookieFilter;
    private final CsrfRequireMatcher csrfRequireMatcher;
    private final SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(this::csrfConfig)
                .headers(this::headersConfig)
                .sessionManagement(this::sessionConfig)
                .authorizeHttpRequests(this::authorizeRequestsConfig)
                .exceptionHandling(this::exceptionHandlingConfig)
                .cors(this::corsConfig)
                .addFilterBefore(sessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(csrfCookieFilter, SessionAuthenticationFilter.class)
                .build();
    }

    private void csrfConfig(CsrfConfigurer<HttpSecurity> csrf) {
        CookieCsrfTokenRepository csrfTokenRepository = getCookieCsrfTokenRepository();
        csrf.csrfTokenRepository(csrfTokenRepository)
                .requireCsrfProtectionMatcher(csrfRequireMatcher)
                .csrfTokenRequestHandler(spaCsrfTokenRequestHandler);
    }

    /**
     * HttpOnly 속성을 추가하지 않은 이유는 Synchronizer Token Pattern을 사용하기 때문입니다.
     * SameSite 속성을 Lax로 설정하여 CSRF 공격을 방지합니다. (더 높은 보안 수준을 원하는 경우 Strict로 설정)
     * Path 속성을 /로 설정하여 모든 범위에서 사용할 수 있도록 합니다.
     * 개발 환경에서 HTTPS 설정하지 않기 때문에 Secure 속성을 추가하지 않았습니다. (향후 HTTPS 설정 시 추가)
     */
    private CookieCsrfTokenRepository getCookieCsrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieCustomizer(cookie -> {
            cookie.sameSite("Lax");
            cookie.path("/");
            cookie.secure(false);
        });
        return csrfTokenRepository;
    }

    private void headersConfig(HeadersConfigurer<HttpSecurity> headers) {
        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
    }

    private void sessionConfig(SessionManagementConfigurer<HttpSecurity> sessionManagement) {
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    private void authorizeRequestsConfig(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests) {
        authorizeRequests
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/v1/login/**").permitAll()
                .requestMatchers("/api/v1/users/**", "/api/v1/verification/**").authenticated()
                .anyRequest().authenticated();
    }

    private void exceptionHandlingConfig(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling) {
        exceptionHandling
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
    }

    private void corsConfig(CorsConfigurer<HttpSecurity> cors) {
        cors.configurationSource(corsConfigurationSource());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Access-Control-Allow-Credentials", "Authorization", "Set-Cookie"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
