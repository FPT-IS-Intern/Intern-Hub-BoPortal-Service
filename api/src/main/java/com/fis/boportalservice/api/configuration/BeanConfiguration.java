package com.fis.boportalservice.api.configuration;

import com.fis.boportalservice.core.util.LogMaskingUtils;
import com.fis.boportalservice.core.util.LoggingProperties;
import com.fis.boportalservice.core.util.SensitiveValueMasker;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigInteger;

@Configuration
public class BeanConfiguration {

  public static final String SECURITY_SCHEME_NAME = "bearerAuth";
  public static final String VERSION = "1.0";
  public static final String DESCRIPTION = "Stock service";

  /**
   * OpenAPI bean.
   *
   * @param title String
   * @return OpenAPI
   */
  @Bean
  public OpenAPI customOpenAPI(@Value("${spring.application.name}") final String title) {
    SecurityScheme securityScheme = new SecurityScheme()
        .name("Authorization")
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");
    return new OpenAPI()
        .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
        .info(new Info().title(title).version(VERSION).description(DESCRIPTION));
  }

  @Bean
  public ObjectMapper objectMapper() {
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
    simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);

    return JsonMapper.builder()
        .findAndAddModules()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .addModule(simpleModule)
        .build();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  @ConfigurationProperties(prefix = "logging")
  public LoggingProperties loggingProperties() {
    return new LoggingProperties();
  }

  @Bean
  public SensitiveValueMasker sensitiveValueMasker(LoggingProperties loggingProperties) {
    return new SensitiveValueMasker(loggingProperties);
  }

  @Bean
  public LogMaskingUtils logMaskingUtils(LoggingProperties loggingProperties) {
    return new LogMaskingUtils(loggingProperties);
  }

}
