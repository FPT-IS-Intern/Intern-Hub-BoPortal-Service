package com.fis.boportalservice.infra.configuration;

import feign.Logger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class FeignClientCommonConfiguration {

  @org.springframework.beans.factory.annotation.Value("${security.internal-secret:defaultSecretValue}")
  private String internalSecret;

  @org.springframework.context.annotation.Bean
  public feign.RequestInterceptor internalSecretInterceptor() {
    return requestTemplate -> {
      requestTemplate.header("X-Internal-Secret", internalSecret);

      RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
      if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
        return;
      }

      HttpServletRequest request = servletRequestAttributes.getRequest();
      String authorization = request.getHeader("Authorization");
      if (authorization != null && !authorization.isBlank()) {
        requestTemplate.header("Authorization", authorization);
      }
    };
  }

  @Bean
  public Logger.Level loggerLevel() {
    return Logger.Level.FULL;
  }

}
