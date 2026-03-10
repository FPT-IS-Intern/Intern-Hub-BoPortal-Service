package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.HomepageBanner;
import com.fis.boportalservice.infra.persistence.entity.HomepageBannerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HomepageBannerEntityMapper {
  HomepageBanner toDomain(HomepageBannerEntity entity);

  HomepageBannerEntity toEntity(HomepageBanner domain);
}
