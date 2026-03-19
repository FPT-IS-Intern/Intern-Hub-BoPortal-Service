package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.SystemConfigRequest;
import com.fis.boportalservice.api.dto.response.SystemConfigPublicResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigWorkingTimeResponse;
import com.fis.boportalservice.core.domain.model.SystemConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemConfigApiMapper {
  SystemConfigPublicResponse toPublicResponse(SystemConfig domain);

  SystemConfigWorkingTimeResponse toWorkingTimeResponse(SystemConfig domain);

  SystemConfig toDomain(SystemConfigRequest request);
}
