package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.HomepageBannerRequest;
import com.fis.boportalservice.api.dto.response.HomepageBannerResponse;
import com.fis.boportalservice.core.domain.model.HomepageBanner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HomepageBannerApiMapper {
  HomepageBannerResponse toResponse(HomepageBanner domain);

  @Mapping(target = "id", ignore = true)
  HomepageBanner toDomain(HomepageBannerRequest request);
}
