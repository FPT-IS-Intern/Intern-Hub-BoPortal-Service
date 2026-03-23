package com.fis.boportalservice.core.domain.usecase.impl;

import com.fis.boportalservice.core.domain.model.BoAdminUser;
import com.fis.boportalservice.core.domain.port.PasswordHasher;
import com.fis.boportalservice.core.domain.repository.BoAdminUserRepository;
import com.fis.boportalservice.core.domain.repository.BoRefreshTokenRepository;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BoAuthUsecaseImplTest {

  private final BoAdminUserRepository boAdminUserRepository = mock(BoAdminUserRepository.class);
  private final BoRefreshTokenRepository boRefreshTokenRepository = mock(BoRefreshTokenRepository.class);
  private final BoTokenProvider boTokenProvider = mock(BoTokenProvider.class);
  private final PasswordHasher passwordHasher = mock(PasswordHasher.class);

  private final BoAuthUsecaseImpl usecase = new BoAuthUsecaseImpl(
      boAdminUserRepository,
      boRefreshTokenRepository,
      boTokenProvider,
      passwordHasher
  );

  @Test
  void login_shouldThrowInvalidCredential_andIncreaseFailedAttempt_whenPasswordDoesNotMatch() {
    BoAdminUser user = BoAdminUser.builder()
        .id(UUID.randomUUID())
        .username("admin")
        .passwordHash("hashed")
        .status("ACTIVE")
        .failedAttempt(1)
        .build();
    when(boAdminUserRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    when(passwordHasher.matches("wrong-password", "hashed")).thenReturn(false);

    ClientSideException exception = assertThrows(
        ClientSideException.class,
        () -> usecase.login("admin", "wrong-password", "device-1")
    );

    assertEquals(ErrorCode.BO_INVALID_CREDENTIAL, exception.getCode());
    assertEquals(2, user.getFailedAttempt());
    verify(boAdminUserRepository).save(user);
    verifyNoInteractions(boTokenProvider, boRefreshTokenRepository);
  }

  @Test
  void login_shouldThrowAccountLocked_whenUserIsStillLocked() {
    BoAdminUser user = BoAdminUser.builder()
        .id(UUID.randomUUID())
        .username("admin")
        .passwordHash("hashed")
        .status("ACTIVE")
        .lockedUntil(LocalDateTime.now().plusMinutes(5))
        .build();
    when(boAdminUserRepository.findByUsername("admin")).thenReturn(Optional.of(user));

    ClientSideException exception = assertThrows(
        ClientSideException.class,
        () -> usecase.login("admin", "any-password", "device-1")
    );

    assertEquals(ErrorCode.BO_ACCOUNT_LOCKED, exception.getCode());
    verify(boAdminUserRepository, never()).save(any());
    verifyNoInteractions(passwordHasher, boTokenProvider, boRefreshTokenRepository);
  }
}
