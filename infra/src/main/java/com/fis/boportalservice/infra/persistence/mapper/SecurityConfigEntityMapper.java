package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.SecurityConfig;
import com.fis.boportalservice.infra.persistence.entity.SecurityConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SecurityConfigEntityMapper {
  SecurityConfig toDomain(SecurityConfigEntity entity);

  void updateEntity(SecurityConfig config, @MappingTarget SecurityConfigEntity entity);
}
