package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.core.domain.repository.SystemConfigRepository;
import com.fis.boportalservice.infra.persistence.entity.SystemConfigEntity;
import com.fis.boportalservice.infra.persistence.mapper.SystemConfigEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.SystemConfigJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SystemConfigRepositoryAdapter implements SystemConfigRepository {

  private static final int CONFIG_ID = 1;

  private final SystemConfigJPARepository jpaRepository;
  private final SystemConfigEntityMapper entityMapper;

  @Override
  public Optional<SystemConfig> find() {
    return jpaRepository.findById(CONFIG_ID).map(entityMapper::toDomain);
  }

  @Override
  public SystemConfig save(SystemConfig config) {
    SystemConfigEntity entity = jpaRepository.findById(CONFIG_ID)
        .orElseThrow(() -> new RuntimeException("System config not initialized"));
    entityMapper.updateEntity(config, entity);
    return entityMapper.toDomain(jpaRepository.save(entity));
  }
}
