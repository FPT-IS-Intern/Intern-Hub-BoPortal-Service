package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.Branch;
import com.fis.boportalservice.infra.persistence.entity.BranchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchEntityMapper {
  Branch toDomain(BranchEntity entity);

  BranchEntity toEntity(Branch domain);
}
