package com.fis.boportalservice.infra.util;

import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.exception.ServerSideException;
import com.fis.boportalservice.infra.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class SecurityInfraHelper {

  private SecurityInfraHelper() {
  }

  public static String sha256Hex(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return Hex.encodeHexString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new ServerSideException("SHA-256 algorithm not found!", e.getMessage());
    }
  }

  public static PrivateKey loadPrivateKey(String privateKey) {
    if (StringUtils.isBlank(privateKey)) {
      throw new ServerSideException(ErrorCode.RESPONSE_ERROR.getCode(), "Private key is blank");
    }
    String key;
    try {
      // Try to get default private key
      log.info("Infra - [loadPrivateKey] - Try load private key by path (default)");
      Resource resource = new ClassPathResource(privateKey);
      InputStream is = resource.getInputStream();
      key = cleanPrivateKey(new String(is.readAllBytes(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      log.info("Infra - [loadPrivateKey] - Load private key from ArgoCD");
      key = cleanPrivateKey(privateKey);
    }
    try {
      byte[] keyBytes = Base64.getDecoder().decode(key);
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance(SecurityConstant.RSA);
      return kf.generatePrivate(spec);
    } catch (Exception e) {
      log.error("Infra - [loadPrivateKey] - Error load private key: {}", e.getMessage());
      throw new ServerSideException(ErrorCode.RESPONSE_ERROR.getCode(), "Error load private key");
    }
  }

  public static PublicKey loadPublicKey(String publicKey) {
    if (StringUtils.isBlank(publicKey)) {
      throw new ServerSideException(ErrorCode.RESPONSE_ERROR.getCode(), "Public Key is blank");
    }
    String key;
    try {
      // Try to get default public key
      log.info("Infra - [loadPublicKey] - Try load public key by path (default)");
      Resource resource = new ClassPathResource(publicKey);
      key =
          cleanPublicKey(
              new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      log.info("Infra - [loadPublicKey] - Load public key from ArgoCD");
      key = cleanPublicKey(publicKey);
    }

    try {
      byte[] keyBytes = Base64.getDecoder().decode(key);
      X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance(SecurityConstant.RSA);
      return kf.generatePublic(spec);
    } catch (Exception e) {
      throw new ServerSideException(ErrorCode.RESPONSE_ERROR.getCode(), e.getMessage());
    }
  }

  public static String signatureData(String data, PrivateKey privateKey) {
    try {
      Signature sig = Signature.getInstance(SecurityConstant.SIGNATURE_ALGORITHM_SHA512);
      sig.initSign(privateKey);
      sig.update(data.getBytes());
      log.info("Infra - [signatureData] - Data signing successful");
      return Base64.getEncoder().encodeToString(sig.sign());
    } catch (Exception e) {
      throw new ServerSideException(ErrorCode.RESPONSE_ERROR.getCode(), e.getMessage());
    }
  }

  public static boolean verifySignature(String data, String base64Signature, PublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance(SecurityConstant.SIGNATURE_ALGORITHM_SHA512);
      signature.initVerify(publicKey);
      signature.update(data.getBytes(StandardCharsets.UTF_8));
      boolean verifyResult = signature.verify(Base64.getDecoder().decode(base64Signature));
      log.info("Infra - [verifySignature] - Verify Signature result: {}", verifyResult);
      return verifyResult;
    } catch (Exception e) {
      throw new ServerSideException(ErrorCode.RESPONSE_ERROR.getCode(), e.getMessage());
    }
  }

  public static String cleanPrivateKey(String key) {
    return key.replace(SecurityConstant.BEGIN_PRIVATE_KEY, "")
        .replace(SecurityConstant.END_PRIVATE_KEY, "")
        .replaceAll("\\s", "");
  }

  public static String cleanPublicKey(String key) {
    return key.replace(SecurityConstant.BEGIN_PUBLIC_KEY, "")
        .replace(SecurityConstant.END_PUBLIC_KEY, "")
        .replaceAll("\\s", "");
  }
}
