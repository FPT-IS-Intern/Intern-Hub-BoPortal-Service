package com.fis.boportalservice.api.configuration;

import com.fis.boportalservice.api.configuration.security.JwtAuthenticationFilter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        @Value("${security.cors.allowed-origin-patterns:https://internhub-v2.bbtech.io.vn}")
        private String allowedOriginPatterns;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http.csrf(AbstractHttpConfigurer::disable)
                        .cors(cors -> {
                        })
                        .sessionManagement(
                                configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .headers(
                                configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/bo-portal/auth/login",
                                        "/bo-portal/auth/refresh",
                                        "/bo-portal/internal/**",
                                        "/actuator/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/docs/**",
                                        "/webjars/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(Arrays.stream(allowedOriginPatterns.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isBlank())
                                .toList());
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setExposedHeaders(List.of("Authorization"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
