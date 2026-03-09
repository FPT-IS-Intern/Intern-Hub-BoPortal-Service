package com.fis.boportalservice.api.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.boportalservice.api.dto.LoginUserInfo;
import com.fis.boportalservice.common.dto.ResponseApi;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected final void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        if (isBypassPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtTokenProvider.extractJwtFromRequest(request);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token, request)) {
                Claims claims = jwtTokenProvider.getClaimsFromToken(token);

                String json = objectMapper.writeValueAsString(claims);
                LoginUserInfo loginUserInfo = objectMapper.readValue(json, LoginUserInfo.class);

                if (loginUserInfo != null) {
                    MDC.put("clientNo", loginUserInfo.INDI.clientNo);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(loginUserInfo, null, null);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                log.error("Unauthorized access - Missing JWT Token {}", request.getRequestId());
                writeErrorResponse(response, "Missing JWT Token");
                return;
            }
        } catch (Exception ex) {
            log.error("Unauthorized access - Invalid JWT Token: {}. Error: {}", ex.getMessage(), ex);
            writeErrorResponse(response, "Unauthorized - Invalid Token");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isBypassPath(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String path = request.getRequestURI();
        return antPathMatcher.match("/actuator/**", path)
                || antPathMatcher.match("/docs/**", path)
                || antPathMatcher.match("/swagger-ui/**", path)
                || antPathMatcher.match("/swagger-ui.html", path)
                || antPathMatcher.match("/v3/api-docs/**", path)
                || antPathMatcher.match("/swagger-resources/**", path)
                || antPathMatcher.match("/webjars/**", path)
                || antPathMatcher.match("/bo-portal/internal/**", path)
                || antPathMatcher.match("/bo-portal/auth/login", path)
                || antPathMatcher.match("/bo-portal/auth/refresh", path);
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ResponseApi<?> res =
                ResponseApi.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), message);
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
