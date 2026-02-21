package com.fis.boportalservice.infra.constant;

public class SecurityConstant {

    private SecurityConstant() {}

    public static final String SIGNATURE_ALGORITHM_SHA512 = "SHA512withRSA";
    public static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    public static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    public static final String RSA = "RSA";
    public static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    public static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
}
