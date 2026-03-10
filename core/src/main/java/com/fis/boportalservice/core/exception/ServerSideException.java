package com.fis.boportalservice.core.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServerSideException extends RuntimeException {
  private final String code;
  private final String message;
  private final Object[] messageArgs;
  private final String category;
  private final Object data;
  private final Exception exception;

  @Builder
  private ServerSideException(
      String code,
      String message,
      Object[] messageArgs,
      String category,
      Object data,
      Exception exception) {
    super(message, exception);
    this.code = code;
    this.message = message;
    this.messageArgs = messageArgs;
    this.category = category;
    this.data = data;
    this.exception = exception;
  }

  public ServerSideException(String code) {
    super();
    this.code = code;
    this.message = null;
    this.messageArgs = null;
    this.category = null;
    this.data = null;
    this.exception = null;
  }

  public ServerSideException(String code, String message) {
    super(message);
    this.code = code;
    this.message = message;
    this.messageArgs = null;
    this.category = null;
    this.data = null;
    this.exception = null;
  }

  public ServerSideException(String code, String message, Exception exception) {
    super(message, exception);
    this.code = code;
    this.message = message;
    this.messageArgs = null;
    this.category = null;
    this.data = null;
    this.exception = exception;
  }
}
