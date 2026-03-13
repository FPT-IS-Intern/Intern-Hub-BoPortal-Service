package com.fis.boportalservice.api.exception;

import com.fis.boportalservice.api.util.APIHelper;
import com.fis.boportalservice.common.dto.FieldError;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.exception.ErrorMessageCatalog;
import com.fis.boportalservice.core.exception.ServerSideException;
import feign.FeignException;
import feign.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionAdvisor extends ResponseEntityExceptionHandler {
  private final MessageSource messageSource;
  private static final List<String> INTERNAL_SERVER_ERROR_CODES =
      List.of(ErrorCode.SYSTEM_ERROR.getCode());
  private static final Pattern CONSTRAINT_PATTERN =
      Pattern.compile("constraint\\s+\\\"([^\\\"]+)\\\"", Pattern.CASE_INSENSITIVE);
  private static final Map<String, ErrorCode> CONSTRAINT_ERROR_MAP = createConstraintErrorMap();

  @ExceptionHandler(ClientSideException.class)
  public ResponseEntity<Object> adviceClientSideException(
      ClientSideException exception, WebRequest request, Locale locale) {
    String message =
        getErrorMessage(
            exception.getCode(), exception.getMessage(), exception.getMessageArgs(), locale);
    log.error("A client side exception occurred: {}", message);
    return new ResponseEntity<>(
        ResponseApi.error(exception.getCode().getCode(), message, exception.getData()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ServerSideException.class)
  public ResponseEntity<Object> adviceServerSideException(
      ServerSideException exception, WebRequest request, Locale locale) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String code = exception.getCode();
    String message = exception.getMessage();
    Object[] messageArgs = exception.getMessageArgs();
    String category = exception.getCategory();
    ErrorCode errorCode = ErrorCode.fromRawCodeAndCategory(code, category);

    if (errorCode != null) {
      code = errorCode.getCode();
      message = getErrorMessage(errorCode, message, messageArgs, locale);

      if (!INTERNAL_SERVER_ERROR_CODES.contains(code)) {
        status = HttpStatus.BAD_REQUEST;
      }
    } else {
      code = ErrorCode.BAD_REQUEST.getCode();
      message = getErrorMessage(ErrorCode.BAD_REQUEST, null, messageArgs, locale);
    }

    log.error("A server side exception occurred: {}", message);
    return new ResponseEntity<>(ResponseApi.error(code, message), status);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<Object> handleFeignException(
      FeignException exception, WebRequest request, Locale locale) {
    HttpStatus status =
        Optional.ofNullable(HttpStatus.resolve(exception.status()))
            .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    Throwable cause = ExceptionUtils.getRootCause(exception);
    String code;
    String message = exception.getMessage();
    Object[] messageArgs;

    if (cause instanceof ClientSideException) {
      code = ((ClientSideException) cause).getCode().getCode();
      messageArgs = ((ClientSideException) cause).getMessageArgs();
      message =
          getErrorMessage(((ClientSideException) cause).getCode(), message, messageArgs, locale);
      status = HttpStatus.BAD_REQUEST;
    } else if (cause instanceof ServerSideException) {
      code = ((ServerSideException) cause).getCode();
      messageArgs = ((ServerSideException) cause).getMessageArgs();
      ErrorCode errorCode =
          ErrorCode.fromRawCodeAndCategory(code, ((ServerSideException) cause).getCategory());
      status = HttpStatus.BAD_REQUEST;

      if (errorCode != null) {
        code = errorCode.getCode();
        message = getErrorMessage(errorCode, message, messageArgs, locale);

        if (INTERNAL_SERVER_ERROR_CODES.contains(code)) {
          status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
      } else {
        code = ErrorCode.BAD_REQUEST.getCode();
        message = getErrorMessage(null, message, messageArgs, locale);
      }
    } else {
      code = APIHelper.toErrorBusinessCode(status).getCode();

      if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
        message = getErrorMessage(ErrorCode.SYSTEM_ERROR, null, null, locale);
      }
    }

    log.error("A feign exception occurred: {}", message);
    return new ResponseEntity<>(ResponseApi.error(code, message), status);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityViolation(
      DataIntegrityViolationException exception, WebRequest request, Locale locale) {
    log.error("A data integrity violation occurred", exception);
    ErrorCode errorCode = resolveConstraintErrorCode(exception);
    String message = getErrorMessage(errorCode, null, null, locale);
    return new ResponseEntity<>(
        ResponseApi.error(errorCode.getCode(), message),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(Exception exception, WebRequest request) {
    log.error("A internal exception occurred: {}", exception.getMessage(), exception);
    return new ResponseEntity<>(
        ResponseApi.error(
            ErrorCode.SYSTEM_ERROR.getCode(),
            getErrorMessage(ErrorCode.SYSTEM_ERROR, null, null, Locale.getDefault())),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.warn(
        "[handleMethodArgumentNotValid] Validation failed with {} error(s)",
        exception.getMessage());
    List<FieldError> errors = new ArrayList<>();
    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.add(new FieldError(error.getField(), error.getDefaultMessage())));
    return new ResponseEntity<>(
        ResponseApi.error(
            ErrorCode.BAD_REQUEST.getCode(),
            getErrorMessage(ErrorCode.BAD_REQUEST, "Validation failed", null, Locale.getDefault()),
            errors),
        HttpStatus.BAD_REQUEST);
  }

  private String getErrorMessage(
      ErrorCode code, String message, Object[] messageArgs, Locale locale) {
    if (code != null) {
      String resolvedFromCode =
          resolveMessageByKey(ErrorMessageCatalog.getMessageKey(code), messageArgs, locale);
      if (resolvedFromCode != null) {
        return resolvedFromCode;
      }
      if (message == null || message.isBlank()) {
        return code.getDescription();
      }
    }

    if (message == null || message.isBlank()) {
      return ErrorCode.SYSTEM_ERROR.getDescription();
    }

    String resolvedFromFallback =
        resolveMessageByKey(message, messageArgs, locale);
    if (resolvedFromFallback != null) {
      return resolvedFromFallback;
    }
    return message;
  }

  private String resolveMessageByKey(String key, Object[] messageArgs, Locale locale) {
    if (key == null || key.isBlank()) {
      return null;
    }
    try {
      Object[] args = messageArgs != null ? messageArgs : new Object[]{};
      return messageSource.getMessage(key, args, locale);
    } catch (Exception e) {
      return null;
    }
  }

  private static Map<String, ErrorCode> createConstraintErrorMap() {
    Map<String, ErrorCode> map = new HashMap<>();
    map.put("branches_name_key", ErrorCode.BRANCH_NAME_DUPLICATE);
    map.put("uk_bo_role_code", ErrorCode.BO_ROLE_CODE_DUPLICATE);
    map.put("uk_bo_permission_code", ErrorCode.BO_PERMISSION_CODE_DUPLICATE);
    map.put("uk_bo_admin_user_username", ErrorCode.BO_USERNAME_DUPLICATE);
    map.put("uk_bo_user_role", ErrorCode.BO_USER_ROLE_DUPLICATE);
    map.put("uk_bo_role_permission", ErrorCode.BO_ROLE_PERMISSION_DUPLICATE);
    map.put("uk_bo_refresh_token_hash", ErrorCode.BO_REFRESH_TOKEN_DUPLICATE);
    return map;
  }

  private ErrorCode resolveConstraintErrorCode(Throwable exception) {
    String constraint = extractConstraintName(exception);
    if (constraint == null || constraint.isBlank()) {
      return ErrorCode.BAD_REQUEST;
    }
    return CONSTRAINT_ERROR_MAP.getOrDefault(constraint, ErrorCode.BAD_REQUEST);
  }

  private String extractConstraintName(Throwable exception) {
    Throwable current = exception;
    while (current != null) {
      String message = current.getMessage();
      if (message != null) {
        Matcher matcher = CONSTRAINT_PATTERN.matcher(message);
        if (matcher.find()) {
          return matcher.group(1);
        }
      }
      current = current.getCause();
    }
    return null;
  }
}
