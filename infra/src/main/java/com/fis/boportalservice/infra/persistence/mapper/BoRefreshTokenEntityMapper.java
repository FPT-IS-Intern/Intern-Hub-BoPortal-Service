package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.BoRefreshToken;
import com.fis.boportalservice.infra.persistence.entity.BoRefreshTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoRefreshTokenEntityMapper {
  BoRefreshToken toDomain(BoRefreshTokenEntity entity);

  BoRefreshTokenEntity toEntity(BoRefreshToken domain);
}
