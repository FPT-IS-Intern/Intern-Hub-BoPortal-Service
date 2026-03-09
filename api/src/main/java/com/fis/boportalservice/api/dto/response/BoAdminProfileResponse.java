package com.fis.boportalservice.api.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoAdminProfileResponse {
    private UUID id;
    private String username;
    private String displayName;
    private List<String> roles;
    private List<String> permissions;
}

