package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.infra.persistence.entity.PortalMenuEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PortalMenuEntityMapper {
    PortalMenu toDomain(PortalMenuEntity entity);

    PortalMenuEntity toEntity(PortalMenu domain);
}
