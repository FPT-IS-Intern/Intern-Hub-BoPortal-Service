package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.BoTokenClaims;

public interface BoTokenProvider {
    String generateAccessToken(BoTokenClaims claims);

    String generateRefreshToken(BoTokenClaims claims);

    BoTokenClaims parseAccessToken(String token);

    BoTokenClaims parseRefreshToken(String token);

    long getAccessTokenExpiresInSeconds();

    long getRefreshTokenExpiresInSeconds();
}
