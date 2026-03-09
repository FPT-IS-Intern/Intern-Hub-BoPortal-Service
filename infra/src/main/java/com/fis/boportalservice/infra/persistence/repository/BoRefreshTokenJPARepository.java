package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.BoRefreshTokenEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoRefreshTokenJPARepository extends JpaRepository<BoRefreshTokenEntity, UUID> {
    Optional<BoRefreshTokenEntity> findByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE BoRefreshTokenEntity t SET t.revokedAt = :revokedAt WHERE t.id = :id")
    void revokeById(@Param("id") UUID id, @Param("revokedAt") LocalDateTime revokedAt);
}

