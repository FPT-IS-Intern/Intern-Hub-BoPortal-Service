package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.SecurityConfig;
import com.fis.boportalservice.core.domain.repository.SecurityConfigRepository;
import com.fis.boportalservice.infra.persistence.entity.SecurityConfigEntity;
import com.fis.boportalservice.infra.persistence.mapper.SecurityConfigEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.SecurityConfigJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityConfigRepositoryAdapter implements SecurityConfigRepository {

  private final SecurityConfigJPARepository jpaRepository;
  private final SecurityConfigEntityMapper entityMapper;

  @Override
  public Optional<SecurityConfig> find() {
    return jpaRepository.findTopByOrderByCreatedAtDesc().map(entityMapper::toDomain);
  }

  @Override
  public SecurityConfig save(SecurityConfig config) {
    SecurityConfigEntity entity = jpaRepository.findTopByOrderByCreatedAtDesc()
        .orElseThrow(() -> new RuntimeException("Security config not initialized"));
    entityMapper.updateEntity(config, entity);
    return entityMapper.toDomain(jpaRepository.save(entity));
  }
}
