package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.infra.persistence.entity.SystemConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SystemConfigEntityMapper {
    SystemConfig toDomain(SystemConfigEntity entity);

    void updateEntity(SystemConfig config, @MappingTarget SystemConfigEntity entity);
}
