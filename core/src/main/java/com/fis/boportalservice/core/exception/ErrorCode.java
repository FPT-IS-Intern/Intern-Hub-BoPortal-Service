package com.fis.boportalservice.core.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST("0400", "0400", "SYSTEM", "Invalid request", "error.invalid_request"),
    REQUEST_TIMEOUT("0408", "0408", "SYSTEM", "Request timeout", "error.request_timeout"),
    INVALID_SIGNATURE("0444", "0444", "SYSTEM", "Invalid signature", "Invalid signature"),
    TIMEOUT("0504", "0504", "SYSTEM", "Gateway timeout", "error.gateway_timeout"),
    RESPONSE_ERROR(
            "0501",
            "0501",
            "SYSTEM",
            "External service response error",
            "error.external_service_response_error"),
    SERVICE_UNAVAILABLE("0503", "0503", "SYSTEM", "Service unavailable", "error.service_unavailable"),
    SYSTEM_ERROR("0500", "0500", "SYSTEM", "System error", "error.system_error"),
    NOT_FOUND("0404", "0404", "SYSTEM", "Not found", "error.not_found"),
    BO_INVALID_CREDENTIAL("1401", "1401", "AUTH", "Invalid credential", "error.bo_auth.invalid_credential"),
    BO_ACCOUNT_INACTIVE("1402", "1402", "AUTH", "Account is inactive", "error.bo_auth.account_inactive"),
    BO_ACCOUNT_LOCKED("1403", "1403", "AUTH", "Account is temporarily locked", "error.bo_auth.account_locked"),
    BO_REFRESH_TOKEN_NOT_FOUND(
            "1404",
            "1404",
            "AUTH",
            "Refresh token not found",
            "error.bo_auth.refresh_token_not_found"),
    BO_REFRESH_TOKEN_INVALID(
            "1405",
            "1405",
            "AUTH",
            "Refresh token expired or revoked",
            "error.bo_auth.refresh_token_invalid"),
    BO_TOKEN_DEVICE_MISMATCH(
            "1406",
            "1406",
            "AUTH",
            "Token device mismatch",
            "error.bo_auth.token_device_mismatch"),
    BO_REFRESH_TOKEN_USER_MISMATCH(
            "1407",
            "1407",
            "AUTH",
            "Refresh token user mismatch",
            "error.bo_auth.refresh_token_user_mismatch"),
    BO_USER_NOT_FOUND("1408", "1408", "AUTH", "User not found", "error.bo_auth.user_not_found"),
    BO_MISSING_ACCESS_TOKEN(
            "1409",
            "1409",
            "AUTH",
            "Missing access token",
            "error.bo_auth.missing_access_token"),
    BO_TOKEN_PROCESS_FAILED(
            "1410",
            "1410",
            "AUTH",
            "Cannot process token",
            "error.bo_auth.token_process_failed");
    private final String code;
    private final String rawCode;
    private final String category;
    private final String description;
    private final String message; // message = null nghĩa là lấy message từ nơi khác
    private static final Map<String, ErrorCode> ENUM_MAP;

    ErrorCode(String code, String rawCode, String category, String description, String message) {
        this.code = code;
        this.rawCode = rawCode;
        this.category = category;
        this.description = description;
        this.message = message;
    }

    static {
        final Map<String, ErrorCode> map = new HashMap<>();
        for (ErrorCode instance : ErrorCode.values()) {
            map.put(instance.getCode(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static ErrorCode get(String value) {
        return ENUM_MAP.get(value);
    }

    public static ErrorCode fromRawCodeAndCategory(String rawCode, String category) {
        if (rawCode == null || rawCode.isBlank()) {
            return null;
        }

        for (ErrorCode errorCode : values()) {
            if (errorCode.rawCode.equalsIgnoreCase(rawCode.trim())) {
                if (category != null && !category.isBlank()) {
                    if (errorCode.category.equalsIgnoreCase(category.trim())) {
                        return errorCode;
                    }
                } else {
                    return errorCode;
                }
            }
        }

        return null;
    }
}
