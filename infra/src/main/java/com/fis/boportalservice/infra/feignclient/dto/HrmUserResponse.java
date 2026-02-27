package com.fis.boportalservice.infra.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HrmUserResponse {
    private Long userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String positionCode;
    private String role;
    private String status;
}
