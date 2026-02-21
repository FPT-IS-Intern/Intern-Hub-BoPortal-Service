package com.fis.boportalservice.infra.configuration;

import com.fis.boportalservice.core.util.LogMaskingUtils;
import com.fis.boportalservice.infra.constant.FeignConstant;
import feign.Logger;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;

@Slf4j
public class FeignClientCommonConfiguration {

    protected LogMaskingUtils logMaskingUtil = new LogMaskingUtils();

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }

    protected String resolveRequestId() {
        return Optional.ofNullable(MDC.get(FeignConstant.X_REQUEST_ID))
                .orElseGet(() -> UUID.randomUUID().toString());
    }

    protected String resolveCorrelationId() {
        return Optional.ofNullable(MDC.get(FeignConstant.X_Correlation_ID))
                .orElseGet(() -> UUID.randomUUID().toString());
    }
}
