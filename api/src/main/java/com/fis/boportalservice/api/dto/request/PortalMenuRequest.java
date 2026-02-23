package com.fis.boportalservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortalMenuRequest {
    @NotBlank(message = "Menu code is required")
    private String code;

    @NotBlank(message = "Menu title is required")
    private String title;

    private String path;
    private String icon;
    private Integer parentId;
    private String permissionCode;
    private Integer sortOrder;
    private String status;
}
