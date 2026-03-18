package com.fis.boportalservice.core.domain.port;

public interface PasswordHasher {
  boolean matches(String rawPassword, String hashedPassword);

  String hash(String rawPassword);
}
