package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.request.BranchRequest;
import com.fis.boportalservice.api.dto.response.BranchResponse;
import com.fis.boportalservice.core.domain.model.Branch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchApiMapper {
    BranchResponse toResponse(Branch domain);

    Branch toDomain(BranchRequest request);
}
