package com.fis.boportalservice.infra.constant;

public class FeignConstant {
  private FeignConstant() {
  }

  public static final String X_REQUEST_ID = "X-REQUEST-ID";
  public static final String X_REQUEST_TIME = "X-Request-Time";
  public static final String X_Correlation_ID = "X-Correlation-ID";
  public static final String X_CLIENT_ID = "X-CLIENT-ID";

  public static final String CONTENT_TYPE = "Content-Type";
  public static final String DATE = "Date";
  public static final String CHANNEL = "channel";
  public static final String CHANNEL_VALUE = "IBANKING";

  public static final String FEIGN_CLIENT_ESB_AUTH = "FEIGN-CLIENT-ESB-AUTH";
  public static final String FEIGN_CLIENT_ESB = "FEIGN-CLIENT-ESB";

  public static final String VI = "vi";
  public static final String EN = "en";
}
