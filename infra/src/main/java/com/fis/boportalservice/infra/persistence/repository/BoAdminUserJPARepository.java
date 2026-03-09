package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.BoAdminUserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoAdminUserJPARepository extends JpaRepository<BoAdminUserEntity, UUID> {
    Optional<BoAdminUserEntity> findByUsernameIgnoreCase(String username);
}

