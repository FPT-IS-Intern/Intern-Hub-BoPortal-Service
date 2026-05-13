package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.infra.persistence.entity.PortalMenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PortalMenuEntityMapper {
  @Mapping(target = "roleCodes", ignore = true)
  PortalMenu toDomain(PortalMenuEntity entity);

  PortalMenuEntity toEntity(PortalMenu domain);
}
