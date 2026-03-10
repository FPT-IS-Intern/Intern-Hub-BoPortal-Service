package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.AllowedIpRange;
import com.fis.boportalservice.infra.persistence.entity.AllowedIpRangeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AllowedIpRangeEntityMapper {
  AllowedIpRange toDomain(AllowedIpRangeEntity entity);

  AllowedIpRangeEntity toEntity(AllowedIpRange domain);
}
