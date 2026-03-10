package com.fis.boportalservice.api.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.BoTokenClaims;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final BoTokenProvider boTokenProvider;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected final void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        // Generate a RequestId for logging
        String requestId = request.getHeader("X-Request-Id");
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put("RequestId", requestId);

        try {
            if (isBypassPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                String token = extractBearerToken(request);

                if (!StringUtils.hasText(token)) {
                    log.error("Unauthorized access - Missing JWT Token for {}", request.getRequestURI());
                    writeErrorResponse(response, "Missing JWT Token");
                    return;
                }

                BoTokenClaims claims = boTokenProvider.parseAccessToken(token);

                if (claims != null && claims.getUserId() != null) {
                    MDC.put("clientNo", claims.getUserId().toString());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims,
                            null, null);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                log.error("Unauthorized access - Invalid JWT Token: {}", ex.getMessage());
                writeErrorResponse(response, "Unauthorized - Invalid Token");
                return;
            }
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            SecurityContextHolder.clearContext();
        }
    }

    private String extractBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private boolean isBypassPath(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String path = normalizePath(request.getRequestURI());
        return antPathMatcher.match("/actuator/**", path)
                || antPathMatcher.match("/docs/**", path)
                || antPathMatcher.match("/swagger-ui/**", path)
                || antPathMatcher.match("/swagger-ui.html", path)
                || antPathMatcher.match("/v3/api-docs/**", path)
                || antPathMatcher.match("/swagger-resources/**", path)
                || antPathMatcher.match("/webjars/**", path)
                || antPathMatcher.match("/bo-portal/internal/**", path)
                || antPathMatcher.match("/api/bo-portal/internal/**", path)
                || antPathMatcher.match("/bo-portal/auth/login", path)
                || antPathMatcher.match("/api/bo-portal/auth/login", path)
                || antPathMatcher.match("/bo-portal/auth/refresh", path)
                || antPathMatcher.match("/api/bo-portal/auth/refresh", path);
    }

    private String normalizePath(String rawPath) {
        if (!StringUtils.hasText(rawPath)) {
            return "";
        }
        return rawPath.replaceAll("/+", "/");
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ResponseApi<?> res = ResponseApi.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), message);
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
