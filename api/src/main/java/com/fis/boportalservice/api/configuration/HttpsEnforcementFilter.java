package com.fis.boportalservice.api.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class HttpsEnforcementFilter extends OncePerRequestFilter {

  private final boolean requireHttps;

  public HttpsEnforcementFilter(boolean requireHttps) {
    this.requireHttps = requireHttps;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (requireHttps && !isSecureRequest(request)) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
      response.getWriter().write("HTTPS is required");
      return;
    }
    filterChain.doFilter(request, response);
  }

  private boolean isSecureRequest(HttpServletRequest request) {
    if (request.isSecure()) {
      return true;
    }
    String forwardedProto = request.getHeader("X-Forwarded-Proto");
    if (StringUtils.hasText(forwardedProto) && forwardedProto.equalsIgnoreCase("https")) {
      return true;
    }
    String forwarded = request.getHeader("Forwarded");
    return StringUtils.hasText(forwarded) && forwarded.toLowerCase().contains("proto=https");
  }
}
