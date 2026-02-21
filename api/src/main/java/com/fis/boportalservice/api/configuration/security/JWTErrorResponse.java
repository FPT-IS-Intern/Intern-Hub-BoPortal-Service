package com.fis.boportalservice.api.configuration.security;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTErrorResponse {

    private String responseId;
    private String responseCode = "QR-0022";
    private String responseMessage;
    private String responseTime;

    public JWTErrorResponse(String responseMessage) {
        this.responseId = UUID.randomUUID().toString();
        this.responseTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.responseMessage = responseMessage;
    }
}
