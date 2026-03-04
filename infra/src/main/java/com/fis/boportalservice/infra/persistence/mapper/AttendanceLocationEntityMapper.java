package com.fis.boportalservice.infra.persistence.mapper;

import com.fis.boportalservice.core.domain.model.AttendanceLocation;
import com.fis.boportalservice.infra.persistence.entity.AttendanceLocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceLocationEntityMapper {
    AttendanceLocation toDomain(AttendanceLocationEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AttendanceLocationEntity toEntity(AttendanceLocation domain);
}
