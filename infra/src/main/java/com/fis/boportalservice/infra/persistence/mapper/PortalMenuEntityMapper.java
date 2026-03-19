package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.infra.persistence.entity.PortalMenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PortalMenuEntityMapper {
  @Mapping(target = "roleCodes", ignore = true)
  PortalMenu toDomain(PortalMenuEntity entity);

  @Mapping(target = "permissionCode", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  PortalMenuEntity toEntity(PortalMenu domain);
}
