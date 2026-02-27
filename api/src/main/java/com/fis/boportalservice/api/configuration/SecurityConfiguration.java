package com.fis.boportalservice.api.configuration;

import com.fis.boportalservice.api.configuration.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @org.springframework.beans.factory.annotation.Value("${security.internal-path-prefix:/bo-portal/internal/}")
        private String internalPathPrefix;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http.csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(
                                                configurer -> configurer
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .headers(
                                                configurer -> configurer.frameOptions(
                                                                HeadersConfigurer.FrameOptionsConfig::disable))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/bo-portal/docs/**",
                                                                "/actuator/**",
                                                                internalPathPrefix + "**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
