package com.fis.boportalservice.api.configuration;

import io.opentelemetry.api.trace.Span;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "otel.sdk.disabled", havingValue = "false", matchIfMissing = true)
@Component
@Order(1)
public class OpenTelemetryTracingFilter implements Filter {

    public static final String X_REQUEST_ID = "X-Request-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestId = httpServletRequest.getHeader(X_REQUEST_ID);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        Span currentSpan = Span.current();
        String traceId = currentSpan.getSpanContext().getTraceId();
        String spanId = currentSpan.getSpanContext().getSpanId();

        MDC.put("RequestId", requestId);
        MDC.put("TraceId", traceId);
        MDC.put("SpanId", spanId);

        httpServletResponse.setHeader("X-Trace-Id", traceId);
        httpServletResponse.setHeader("X-Request-Id", requestId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
