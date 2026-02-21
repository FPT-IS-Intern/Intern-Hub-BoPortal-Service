package com.fis.boportalservice.infra.configuration;

import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.exception.ServerSideException;
import feign.FeignException;
import feign.utils.ExceptionUtils;
import java.net.ConnectException;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FeignTimeoutAspect {
    @Around("execution(* com.fis.boportalservice.infra.feignclient..*(..))")
    public Object handleFeignTimeout(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            log.error(
                    "Infra - Unexpected error in FeignClient - Duration: {}ms, Error: {}",
                    duration,
                    ex.getMessage());

            Throwable cause = ExceptionUtils.getRootCause(ex);
            String message = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

            if (cause instanceof UnknownHostException) {
                throw new ServerSideException(ErrorCode.SERVICE_UNAVAILABLE.getCode());
            }
            if (cause instanceof FeignException.GatewayTimeout
                    || cause instanceof ConnectException
                    || message.contains("timeout")
                    || message.contains("timed out")) {
                throw new ServerSideException(ErrorCode.TIMEOUT.getCode());
            }
            throw ex;
        }
    }
}
