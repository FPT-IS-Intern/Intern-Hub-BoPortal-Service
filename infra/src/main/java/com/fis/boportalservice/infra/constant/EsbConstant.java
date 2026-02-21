package com.fis.boportalservice.infra.constant;

public class EsbConstant {
    private EsbConstant() {}

    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String SCOPE = "scope";
    public static final String X_IBM_CLIENT_ID = "X-IBM-Client-Id";
    public static final String X_IBM_CLIENT_SECRET = "X-IBM-Client-Secret";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String X_TRACE_ID = "X-TRACE-ID";
    public static final String TRACE_ID = "TraceId";

    public static final String ESB_SUCCESS_STATUS = "0";
    public static final String ESB_SUCCESS_CODE = "000";

    public static final String TOKEN_API = "/oauth2/token";
}
