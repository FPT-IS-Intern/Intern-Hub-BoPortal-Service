package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.response.BoAdminProfileResponse;
import com.fis.boportalservice.api.dto.response.BoAuthSessionResponse;
import com.fis.boportalservice.core.domain.model.BoAdminProfile;
import com.fis.boportalservice.core.domain.model.BoAuthSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoAuthApiMapper {
  BoAuthSessionResponse toSessionResponse(BoAuthSession session);

  BoAdminProfileResponse toProfileResponse(BoAdminProfile profile);
}
