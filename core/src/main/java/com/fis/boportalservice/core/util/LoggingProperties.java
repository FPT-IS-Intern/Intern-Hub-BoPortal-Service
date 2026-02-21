package com.fis.boportalservice.core.util;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "logging")
@Getter
@Setter
public class LoggingProperties {
    private List<String> sensitiveFields = new ArrayList<>();
}
