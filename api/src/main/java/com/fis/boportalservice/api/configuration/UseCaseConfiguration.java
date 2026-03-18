package com.fis.boportalservice.api.configuration;

import com.fis.boportalservice.core.domain.port.PasswordHasher;
import com.fis.boportalservice.core.domain.repository.BoAdminUserRepository;
import com.fis.boportalservice.core.domain.repository.BoRefreshTokenRepository;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.domain.usecase.BoAuthUsecase;
import com.fis.boportalservice.core.domain.usecase.impl.BoAuthUsecaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

  @Bean
  public BoAuthUsecase boAuthUsecase(
      BoAdminUserRepository boAdminUserRepository,
      BoRefreshTokenRepository boRefreshTokenRepository,
      BoTokenProvider boTokenProvider,
      PasswordHasher passwordHasher
  ) {
    return new BoAuthUsecaseImpl(
        boAdminUserRepository,
        boRefreshTokenRepository,
        boTokenProvider,
        passwordHasher
    );
  }
}
