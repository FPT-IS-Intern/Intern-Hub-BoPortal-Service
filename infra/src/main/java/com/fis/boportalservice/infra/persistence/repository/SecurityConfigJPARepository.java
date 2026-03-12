package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.SecurityConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SecurityConfigJPARepository extends JpaRepository<SecurityConfigEntity, UUID> {
  Optional<SecurityConfigEntity> findTopByOrderByCreatedAtDesc();
}
