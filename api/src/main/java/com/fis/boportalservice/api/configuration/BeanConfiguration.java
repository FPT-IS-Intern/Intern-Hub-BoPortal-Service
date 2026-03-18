package com.fis.boportalservice.api.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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

  /**
   * Model resolver bean.
   *
   * @param objectMapper ObjectMapper
   * @return ModelResolver
   */
  @Bean
  public ModelResolver modelResolver(final ObjectMapper objectMapper) {
    return new ModelResolver(
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE));
  }

  @Bean
  public ObjectMapper objectMapper() {
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(Long.class, new ToStringSerializer());
    simpleModule.addSerializer(Long.TYPE, new ToStringSerializer());
    simpleModule.addSerializer(BigInteger.class, new ToStringSerializer());
    // findAndRegisterModules() first, then registerModule() last so our Long→String serializers
    // are registered after any auto-discovered modules and therefore take final precedence.
    ObjectMapper mapper = new ObjectMapper()
        .findAndRegisterModules()
        .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.registerModule(simpleModule);
    return mapper;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
