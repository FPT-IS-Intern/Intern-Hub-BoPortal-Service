package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.BoAdminUser;
import com.fis.boportalservice.infra.persistence.entity.BoAdminUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoAdminUserEntityMapper {
    BoAdminUser toDomain(BoAdminUserEntity entity);

    BoAdminUserEntity toEntity(BoAdminUser domain);
}
