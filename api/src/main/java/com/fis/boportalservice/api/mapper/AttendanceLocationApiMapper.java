package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.AttendanceLocationRequest;
import com.fis.boportalservice.api.dto.response.AttendanceLocationResponse;
import com.fis.boportalservice.core.domain.model.AttendanceLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceLocationApiMapper {
  AttendanceLocationResponse toResponse(AttendanceLocation domain);

  AttendanceLocation toDomain(AttendanceLocationRequest request);
}
