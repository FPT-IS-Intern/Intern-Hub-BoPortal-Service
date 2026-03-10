package com.fis.boportalservice.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fis.boportalservice.core.domain.model.BoTokenClaims;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class BoTokenProviderImpl implements BoTokenProvider {

  private static final String ISSUER = "bo-portal";
  private static final String AUDIENCE = "bo-portal-admin";

  private final Algorithm algorithm;
  private final long accessTokenExpiresInSeconds;
  private final long refreshTokenExpiresInSeconds;

  public BoTokenProviderImpl(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.token.expires-in}") Long accessTokenExpiresInMs,
      @Value("${app.jwt.refresh-token.expires-in}") Long refreshTokenExpiresInMs) {
    if (!StringUtils.hasText(secret)) {
      throw new IllegalStateException("app.jwt.secret must not be empty for BO auth");
    }
    this.algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpiresInSeconds = accessTokenExpiresInMs / 1000;
    this.refreshTokenExpiresInSeconds = refreshTokenExpiresInMs / 1000;
  }

  @Override
  public String generateAccessToken(BoTokenClaims claims) {
    Instant now = Instant.now();
    return JWT.create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withSubject(String.valueOf(claims.getUserId()))
        .withClaim("username", claims.getUsername())
        .withClaim("roles", claims.getRoles())
        .withClaim("permissions", claims.getPermissions())
        .withJWTId(claims.getJti())
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(now.plusSeconds(accessTokenExpiresInSeconds)))
        .sign(algorithm);
  }

  @Override
  public String generateRefreshToken(BoTokenClaims claims) {
    Instant now = Instant.now();
    return JWT.create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withSubject(String.valueOf(claims.getUserId()))
        .withClaim("username", claims.getUsername())
        .withJWTId(claims.getJti())
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(now.plusSeconds(refreshTokenExpiresInSeconds)))
        .sign(algorithm);
  }

  @Override
  public BoTokenClaims parseAccessToken(String token) {
    DecodedJWT jwt = verifyToken(token);
    return toClaims(jwt);
  }

  @Override
  public BoTokenClaims parseRefreshToken(String token) {
    DecodedJWT jwt = verifyToken(token);
    return BoTokenClaims.builder()
        .userId(UUID.fromString(jwt.getSubject()))
        .username(jwt.getClaim("username").asString())
        .jti(jwt.getId())
        .roles(Collections.emptyList())
        .permissions(Collections.emptyList())
        .build();
  }

  @Override
  public long getAccessTokenExpiresInSeconds() {
    return accessTokenExpiresInSeconds;
  }

  @Override
  public long getRefreshTokenExpiresInSeconds() {
    return refreshTokenExpiresInSeconds;
  }

  private DecodedJWT verifyToken(String token) {
    try {
      JWTVerifier verifier =
          JWT.require(algorithm).withIssuer(ISSUER).withAudience(AUDIENCE).build();
      return verifier.verify(token);
    } catch (JWTVerificationException ex) {
      throw new ClientSideException(ErrorCode.BAD_REQUEST, "Invalid token", ex);
    }
  }

  private BoTokenClaims toClaims(DecodedJWT jwt) {
    List<String> roles = jwt.getClaim("roles").asList(String.class);
    List<String> permissions = jwt.getClaim("permissions").asList(String.class);

    return BoTokenClaims.builder()
        .userId(UUID.fromString(jwt.getSubject()))
        .username(jwt.getClaim("username").asString())
        .roles(roles == null ? Collections.emptyList() : roles)
        .permissions(permissions == null ? Collections.emptyList() : permissions)
        .jti(jwt.getId())
        .build();
  }
}

