package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.SecurityConfigRequest;
import com.fis.boportalservice.api.dto.response.SecurityConfigResponse;
import com.fis.boportalservice.core.domain.model.SecurityConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecurityConfigApiMapper {
  SecurityConfigResponse toResponse(SecurityConfig domain);

  SecurityConfig toDomain(SecurityConfigRequest request);
}
