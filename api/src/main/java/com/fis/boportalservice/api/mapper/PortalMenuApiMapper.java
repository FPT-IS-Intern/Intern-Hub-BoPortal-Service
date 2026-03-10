package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.PortalMenuRequest;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.core.domain.model.PortalMenu;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PortalMenuApiMapper {
  PortalMenuResponse toResponse(PortalMenu domain);

  PortalMenu toDomain(PortalMenuRequest request);

  List<PortalMenuResponse> toResponseList(List<PortalMenu> domainList);
}
