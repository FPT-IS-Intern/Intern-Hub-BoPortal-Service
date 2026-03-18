package com.fis.boportalservice.api.dto.request;

import com.fis.boportalservice.api.validation.ValidBoLogin;
@ValidBoLogin
public record BoLoginRequest(
    String username,
    String encryptedUsername,
    String password,
    String encryptedPassword,
    String deviceId
) {
}
