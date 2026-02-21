package com.fis.boportalservice.core.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;

public class RequestIdMdcFilter implements Filter {
    public static final String REQUEST_ID_KEY = "X-Request-ID";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String headerValue = httpRequest.getHeader("X-Request-ID");
        String requestId = headerValue != null ? headerValue : UUID.randomUUID().toString();
        MDC.put("X-Request-ID", requestId);
        filterChain.doFilter(httpRequest, response);
        MDC.clear();
    }
}
