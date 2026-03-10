package com.fis.boportalservice.infra.configuration;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class FeignClientCommonConfiguration {

    @org.springframework.beans.factory.annotation.Value("${security.internal-secret:defaultSecretValue}")
    private String internalSecret;

    @org.springframework.context.annotation.Bean
    public feign.RequestInterceptor internalSecretInterceptor() {
        return requestTemplate -> requestTemplate.header("X-Internal-Secret", internalSecret);
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }

}
