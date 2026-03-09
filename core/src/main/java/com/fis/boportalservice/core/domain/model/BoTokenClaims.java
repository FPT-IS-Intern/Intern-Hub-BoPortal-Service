package com.fis.boportalservice.core.domain.model;

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
public class BoTokenClaims {
    private UUID userId;
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private String jti;
}

