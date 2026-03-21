package com.fis.boportalservice.infra.feignclient.dto;

import java.time.LocalDate;

public record HrmUpdateProfileRequest(
    String fullName,
    String companyEmail,
    LocalDate dateOfBirth,
    String idNumber,
    String address,
    String phoneNumber,
    Long position,
    String sysStatus
) {
}
