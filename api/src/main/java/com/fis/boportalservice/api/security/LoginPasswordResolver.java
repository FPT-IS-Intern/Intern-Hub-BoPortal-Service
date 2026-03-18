package com.fis.boportalservice.api.security;

import com.fis.boportalservice.api.dto.request.BoLoginRequest;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class LoginPasswordResolver {

  @Value("${security.login.rsa-private-key:}")
  private String loginRsaPrivateKey;

  @Value("${security.login.rsa-public-key:}")
  private String loginRsaPublicKey;

  public String resolvePassword(BoLoginRequest request) {
    if (StringUtils.hasText(request.encryptedPassword())) {
      if (!StringUtils.hasText(loginRsaPrivateKey)) {
        log.error("BO login private key is not configured");
        throw new ClientSideException(ErrorCode.SYSTEM_ERROR);
      }
      String decryptedPassword = RSAUtil.decrypt(request.encryptedPassword(), loginRsaPrivateKey);
      if (!StringUtils.hasText(decryptedPassword)) {
        throw new ClientSideException(ErrorCode.BO_INVALID_CREDENTIAL);
      }
      return decryptedPassword;
    }
    return request.password();
  }

  public String resolveUsername(BoLoginRequest request) {
    if (StringUtils.hasText(request.encryptedUsername())) {
      if (!StringUtils.hasText(loginRsaPrivateKey)) {
        log.error("BO login private key is not configured");
        throw new ClientSideException(ErrorCode.SYSTEM_ERROR);
      }
      String decryptedUsername = RSAUtil.decrypt(request.encryptedUsername(), loginRsaPrivateKey);
      if (!StringUtils.hasText(decryptedUsername)) {
        throw new ClientSideException(ErrorCode.BO_INVALID_CREDENTIAL);
      }
      return decryptedUsername;
    }
    return request.username();
  }

  public String getPublicKey() {
    if (!StringUtils.hasText(loginRsaPublicKey)) {
      log.error("BO login public key is not configured");
      throw new ClientSideException(ErrorCode.SYSTEM_ERROR);
    }
    return loginRsaPublicKey;
  }
}
