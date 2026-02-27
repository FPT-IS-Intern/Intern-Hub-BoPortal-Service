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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    @Override
    protected final void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isBypassPath(path)) {
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

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            loginUserInfo, null, null);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                log.error("Unauthorized access - Missing JWT Token for path: {}", path);
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

    private boolean isBypassPath(String path) {
        return path.contains("/actuator")
                || path.contains("/docs/")
                || path.contains("/swagger")
                || path.contains("/internal")
                || path.contains("/webjars");
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ResponseApi<?> res = ResponseApi.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), message);
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
