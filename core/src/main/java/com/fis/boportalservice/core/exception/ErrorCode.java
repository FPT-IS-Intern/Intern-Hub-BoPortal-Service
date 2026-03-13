package com.fis.boportalservice.core.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ErrorCode {
  BAD_REQUEST("0400", "0400", "SYSTEM", "Invalid request"),
  REQUEST_TIMEOUT("0408", "0408", "SYSTEM", "Request timeout"),
  INVALID_SIGNATURE("0444", "0444", "SYSTEM", "Invalid signature"),
  TIMEOUT("0504", "0504", "SYSTEM", "Gateway timeout"),
  RESPONSE_ERROR("0501", "0501", "SYSTEM", "External service response error"),
  SERVICE_UNAVAILABLE("0503", "0503", "SYSTEM", "Service unavailable"),
  SYSTEM_ERROR("0500", "0500", "SYSTEM", "System error"),
  NOT_FOUND("0404", "0404", "SYSTEM", "Not found"),
  BO_INVALID_CREDENTIAL("1401", "1401", "AUTH", "Invalid credential"),
  BO_ACCOUNT_INACTIVE("1402", "1402", "AUTH", "Account is inactive"),
  BO_ACCOUNT_LOCKED("1403", "1403", "AUTH", "Account is temporarily locked"),
  BO_REFRESH_TOKEN_NOT_FOUND("1404", "1404", "AUTH", "Refresh token not found"),
  BO_REFRESH_TOKEN_INVALID("1405", "1405", "AUTH", "Refresh token expired or revoked"),
  BO_TOKEN_DEVICE_MISMATCH("1406", "1406", "AUTH", "Token device mismatch"),
  BO_REFRESH_TOKEN_USER_MISMATCH("1407", "1407", "AUTH", "Refresh token user mismatch"),
  BO_USER_NOT_FOUND("1408", "1408", "AUTH", "User not found"),
  BO_MISSING_ACCESS_TOKEN("1409", "1409", "AUTH", "Missing access token"),
  BO_TOKEN_PROCESS_FAILED("1410", "1410", "AUTH", "Cannot process token"),
  BRANCH_NAME_DUPLICATE("2401", "2401", "BRANCH", "Branch name already exists"),
  BO_ROLE_CODE_DUPLICATE("2402", "2402", "RBAC", "Role code already exists"),
  BO_PERMISSION_CODE_DUPLICATE("2403", "2403", "RBAC", "Permission code already exists"),
  BO_USERNAME_DUPLICATE("2404", "2404", "AUTH", "Username already exists"),
  BO_USER_ROLE_DUPLICATE("2405", "2405", "RBAC", "User already has this role"),
  BO_ROLE_PERMISSION_DUPLICATE("2406", "2406", "RBAC", "Role already has this permission"),
  BO_REFRESH_TOKEN_DUPLICATE("2407", "2407", "AUTH", "Refresh token already exists");

  private final String code;
  private final String rawCode;
  private final String category;
  private final String description;
  private static final Map<String, ErrorCode> ENUM_MAP;

  ErrorCode(String code, String rawCode, String category, String description) {
    this.code = code;
    this.rawCode = rawCode;
    this.category = category;
    this.description = description;
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
