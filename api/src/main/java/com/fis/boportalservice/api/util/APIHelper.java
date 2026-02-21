package com.fis.boportalservice.api.util;

import com.fis.boportalservice.core.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class APIHelper {
    private APIHelper() {}

    public static ErrorCode toErrorBusinessCode(HttpStatus status) {
        if (status == null) {
            log.warn("Converting null HttpStatus to ErrorCode: {}", ErrorCode.RESPONSE_ERROR);
            return ErrorCode.RESPONSE_ERROR;
        }
        if (status == HttpStatus.NOT_FOUND) {
            return ErrorCode.NOT_FOUND;
        }
        if (status == HttpStatus.REQUEST_TIMEOUT) {
            return ErrorCode.TIMEOUT;
        }
        if (status.is4xxClientError()) {
            return ErrorCode.BAD_REQUEST;
        }
        log.info("API - Mapped HttpStatus for Feign Exception {} ", status);
        return ErrorCode.RESPONSE_ERROR;
    }
}
