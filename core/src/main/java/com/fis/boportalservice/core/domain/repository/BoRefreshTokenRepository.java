package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.BoRefreshToken;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BoRefreshTokenRepository {
    BoRefreshToken save(BoRefreshToken token);

    Optional<BoRefreshToken> findByTokenHash(String tokenHash);

    void revokeById(UUID id, LocalDateTime revokedAt);
}

