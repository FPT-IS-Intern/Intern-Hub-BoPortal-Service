package com.fis.boportalservice.infra.security;

import com.fis.boportalservice.core.domain.model.BoTokenClaims;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BoTokenProviderImpl implements BoTokenProvider {

  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private static final String ISSUER = "bo-portal";
  private static final String AUDIENCE = "bo-portal-admin";
  private static final String HEADER_BASE64;

  static {
    String header = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";
    HEADER_BASE64 = Base64.getUrlEncoder().withoutPadding()
        .encodeToString(header.getBytes(StandardCharsets.UTF_8));
  }

  private final Mac mac;
  private final long accessTokenExpiresInSeconds;
  private final long refreshTokenExpiresInSeconds;
  private final ObjectMapper objectMapper;

  public BoTokenProviderImpl(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.token.expires-in}") Long accessTokenExpiresInMs,
      @Value("${app.jwt.refresh-token.expires-in}") Long refreshTokenExpiresInMs) {
    if (!StringUtils.hasText(secret)) {
      throw new IllegalStateException("app.jwt.secret must not be empty for BO auth");
    }
    try {
      SecretKeySpec keySpec = new SecretKeySpec(
          secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
      Mac instance = Mac.getInstance(HMAC_ALGORITHM);
      instance.init(keySpec);
      this.mac = instance;
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new IllegalStateException("Failed to initialize HMAC", e);
    }
    this.accessTokenExpiresInSeconds = accessTokenExpiresInMs / 1000;
    this.refreshTokenExpiresInSeconds = refreshTokenExpiresInMs / 1000;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public String generateAccessToken(BoTokenClaims claims) {
    Instant now = Instant.now();
    long exp = now.getEpochSecond() + accessTokenExpiresInSeconds;
    Map<String, Object> payload = Map.of(
        "iss", ISSUER,
        "aud", AUDIENCE,
        "sub", claims.getUserId().toString(),
        "username", claims.getUsername(),
        "roles", claims.getRoles(),
        "permissions", claims.getPermissions(),
        "jti", claims.getJti(),
        "iat", now.getEpochSecond(),
        "exp", exp
    );
    return encode(payload);
  }

  @Override
  public String generateRefreshToken(BoTokenClaims claims) {
    Instant now = Instant.now();
    long exp = now.getEpochSecond() + refreshTokenExpiresInSeconds;
    Map<String, Object> payload = Map.of(
        "iss", ISSUER,
        "aud", AUDIENCE,
        "sub", claims.getUserId().toString(),
        "username", claims.getUsername(),
        "jti", claims.getJti(),
        "iat", now.getEpochSecond(),
        "exp", exp
    );
    return encode(payload);
  }

  @Override
  public BoTokenClaims parseAccessToken(String token) {
    Map<String, Object> payload = verify(token);
    @SuppressWarnings("unchecked")
    List<String> roles = (List<String>) payload.getOrDefault("roles", Collections.emptyList());
    @SuppressWarnings("unchecked")
    List<String> permissions = (List<String>) payload.getOrDefault("permissions", Collections.emptyList());
    return toClaims(payload, roles, permissions);
  }

  @Override
  public BoTokenClaims parseRefreshToken(String token) {
    Map<String, Object> payload = verify(token);
    return toClaims(payload, Collections.emptyList(), Collections.emptyList());
  }

  @Override
  public long getAccessTokenExpiresInSeconds() {
    return accessTokenExpiresInSeconds;
  }

  @Override
  public long getRefreshTokenExpiresInSeconds() {
    return refreshTokenExpiresInSeconds;
  }

  private String encode(Map<String, Object> payload) {
    try {
      String payloadBase64 = Base64.getUrlEncoder().withoutPadding()
          .encodeToString(objectMapper.writeValueAsBytes(payload));
      String signingInput = HEADER_BASE64 + "." + payloadBase64;
      String signature = sign(signingInput);
      return signingInput + "." + signature;
    } catch (Exception e) {
      throw new ClientSideException(ErrorCode.BO_TOKEN_PROCESS_FAILED, "Failed to generate token", e);
    }
  }

  private Map<String, Object> verify(String token) {
    try {
      String[] parts = token.split("\\.");
      if (parts.length != 3) {
        throw new ClientSideException(ErrorCode.BAD_REQUEST, "Invalid token format");
      }
      String signingInput = parts[0] + "." + parts[1];
      String expectedSignature = sign(signingInput);
      if (!expectedSignature.equals(parts[2])) {
        throw new ClientSideException(ErrorCode.BAD_REQUEST, "Invalid token signature");
      }
      byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
      @SuppressWarnings("unchecked")
      Map<String, Object> payload = objectMapper.readValue(payloadBytes, Map.class);
      long exp = ((Number) payload.get("exp")).longValue();
      if (Instant.now().getEpochSecond() >= exp) {
        throw new ClientSideException(ErrorCode.BAD_REQUEST, "Token expired");
      }
      return payload;
    } catch (ClientSideException e) {
      throw e;
    } catch (Exception e) {
      throw new ClientSideException(ErrorCode.BAD_REQUEST, "Invalid token", e);
    }
  }

  private String sign(String input) {
    byte[] hash = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
    return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
  }

  private BoTokenClaims toClaims(Map<String, Object> payload,
                                  List<String> roles, List<String> permissions) {
    String sub = (String) payload.get("sub");
    return BoTokenClaims.builder()
        .userId(java.util.UUID.fromString(sub))
        .username((String) payload.get("username"))
        .roles(roles)
        .permissions(permissions)
        .jti((String) payload.get("jti"))
        .build();
  }
}
