package com.fis.boportalservice.api.configuration;

import com.fis.boportalservice.api.configuration.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Value("${security.cors.allowed-origin-patterns:http://localhost:4200}")
  private String corsAllowedOriginPatterns;

  @Value("${security.require-https:false}")
  private boolean requireHttps;

  @Value("${security.hsts.enabled:false}")
  private boolean hstsEnabled;

  @Value("${security.hsts.max-age:31536000}")
  private long hstsMaxAgeSeconds;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) {
    HttpSecurity configured = http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/bo-portal/auth/login",
                "/api/bo-portal/auth/login",
                "/bo-portal/auth/public-key",
                "/api/bo-portal/auth/public-key",
                "/bo-portal/auth/refresh",
                "/api/bo-portal/auth/refresh",
                "/bo-portal/internal/**",
                "/api/bo-portal/internal/**",
                "/actuator/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/docs/**",
                "/webjars/**").permitAll()
            .anyRequest().authenticated())
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .addFilterBefore(new HttpsEnforcementFilter(requireHttps), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    if (hstsEnabled) {
      configured = configured.headers(headers -> headers.httpStrictTransportSecurity(
          hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(hstsMaxAgeSeconds)));
    }

    return configured.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    List<String> allowedOriginPatterns = Arrays.stream(corsAllowedOriginPatterns.split(","))
        .map(String::trim)
        .filter(value -> !value.isEmpty())
        .collect(Collectors.toList());
    configuration.setAllowedOriginPatterns(allowedOriginPatterns);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
