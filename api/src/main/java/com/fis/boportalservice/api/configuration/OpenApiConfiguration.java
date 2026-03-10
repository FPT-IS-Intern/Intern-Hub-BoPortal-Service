package com.fis.boportalservice.api.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Value("${services.gateway.url:http://localhost:8765}")
  private String gatewayUrl;

  @Bean
  public OpenApiCustomizer openApiCustomizer() {
    SecurityScheme bearerAuthScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
        .bearerFormat("JWT");
    SecurityScheme internalApiKeyScheme = new SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER)
        .name("X-Internal-Secret");

    return openApi -> openApi
        .addServersItem(new Server().url(gatewayUrl + "/api"))
        .components(
            openApi.getComponents() == null
                ? new Components()
                .addSecuritySchemes("Bearer", bearerAuthScheme)
                .addSecuritySchemes("InternalAPIKey", internalApiKeyScheme)
                : openApi
                .getComponents()
                .addSecuritySchemes("Bearer", bearerAuthScheme)
                .addSecuritySchemes("InternalAPIKey", internalApiKeyScheme));
  }
}
