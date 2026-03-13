package com.fis.boportalservice.core.exception;

import java.util.EnumMap;
import java.util.Map;

public final class ErrorMessageCatalog {
  private static final Map<ErrorCode, String> MESSAGE_KEY_BY_CODE = new EnumMap<>(ErrorCode.class);

  static {
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BAD_REQUEST, "error.invalid_request");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.REQUEST_TIMEOUT, "error.request_timeout");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.INVALID_SIGNATURE, "error.invalid_signature");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.TIMEOUT, "error.gateway_timeout");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.RESPONSE_ERROR, "error.external_service_response_error");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.SERVICE_UNAVAILABLE, "error.service_unavailable");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.SYSTEM_ERROR, "error.system_error");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.NOT_FOUND, "error.not_found");

    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_INVALID_CREDENTIAL, "error.bo_auth.invalid_credential");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_ACCOUNT_INACTIVE, "error.bo_auth.account_inactive");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_ACCOUNT_LOCKED, "error.bo_auth.account_locked");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_REFRESH_TOKEN_NOT_FOUND, "error.bo_auth.refresh_token_not_found");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_REFRESH_TOKEN_INVALID, "error.bo_auth.refresh_token_invalid");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_TOKEN_DEVICE_MISMATCH, "error.bo_auth.token_device_mismatch");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_REFRESH_TOKEN_USER_MISMATCH, "error.bo_auth.refresh_token_user_mismatch");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_USER_NOT_FOUND, "error.bo_auth.user_not_found");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_MISSING_ACCESS_TOKEN, "error.bo_auth.missing_access_token");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_TOKEN_PROCESS_FAILED, "error.bo_auth.token_process_failed");

    MESSAGE_KEY_BY_CODE.put(ErrorCode.BRANCH_NAME_DUPLICATE, "error.branch.duplicate_name");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_ROLE_CODE_DUPLICATE, "error.rbac.role_code_duplicate");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_PERMISSION_CODE_DUPLICATE, "error.rbac.permission_code_duplicate");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_USERNAME_DUPLICATE, "error.auth.username_duplicate");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_USER_ROLE_DUPLICATE, "error.rbac.user_role_duplicate");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_ROLE_PERMISSION_DUPLICATE, "error.rbac.role_permission_duplicate");
    MESSAGE_KEY_BY_CODE.put(ErrorCode.BO_REFRESH_TOKEN_DUPLICATE, "error.auth.refresh_token_duplicate");
  }

  private ErrorMessageCatalog() {
  }

  public static String getMessageKey(ErrorCode code) {
    if (code == null) {
      return null;
    }
    return MESSAGE_KEY_BY_CODE.get(code);
  }
}
