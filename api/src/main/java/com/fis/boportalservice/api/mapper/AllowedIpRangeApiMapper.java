package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.AllowedIpRangeRequest;
import com.fis.boportalservice.api.dto.response.BoPortalAllowedIpRangeResponse;
import com.fis.boportalservice.core.domain.model.AllowedIpRange;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AllowedIpRangeApiMapper {
  BoPortalAllowedIpRangeResponse toResponse(AllowedIpRange domain);

  AllowedIpRange toDomain(AllowedIpRangeRequest request);
}
