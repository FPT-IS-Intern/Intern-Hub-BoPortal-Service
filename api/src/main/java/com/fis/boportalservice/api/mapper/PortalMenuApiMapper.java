package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.PortalMenuRequest;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.core.domain.model.PortalMenu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PortalMenuApiMapper {
  @Mapping(target = "children", ignore = true)
  PortalMenuResponse toResponse(PortalMenu domain);

  @Mapping(target = "id", ignore = true)
  PortalMenu toDomain(PortalMenuRequest request);

  List<PortalMenuResponse> toResponseList(List<PortalMenu> domainList);
}
