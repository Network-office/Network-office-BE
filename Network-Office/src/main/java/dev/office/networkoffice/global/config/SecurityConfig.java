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
        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .requireCsrfProtectionMatcher(csrfRequireMatcher)
                .csrfTokenRequestHandler(spaCsrfTokenRequestHandler);
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
                .requestMatchers("/api/v1/users/**").authenticated()
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
