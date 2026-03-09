package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.BoRefreshToken;
import com.fis.boportalservice.core.domain.repository.BoRefreshTokenRepository;
import com.fis.boportalservice.infra.persistence.entity.BoRefreshTokenEntity;
import com.fis.boportalservice.infra.persistence.mapper.BoRefreshTokenEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.BoRefreshTokenJPARepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoRefreshTokenRepositoryAdapter implements BoRefreshTokenRepository {

    private final BoRefreshTokenJPARepository jpaRepository;
    private final BoRefreshTokenEntityMapper entityMapper;

    @Override
    public BoRefreshToken save(BoRefreshToken token) {
        BoRefreshTokenEntity saved = jpaRepository.save(entityMapper.toEntity(token));
        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<BoRefreshToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash).map(entityMapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeById(UUID id, LocalDateTime revokedAt) {
        jpaRepository.revokeById(id, revokedAt);
    }
}

