package com.fis.boportalservice.core.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class LogMaskingUtils {

    @Value("#{'${logging.sensitive-fields}'.split(',')}")
    private List<String> sensitiveFields;

    public String maskSensitiveFields(String input) {
        if (!StringUtils.hasText(input)) return input;

        if (ObjectUtils.isEmpty(sensitiveFields)) {
            sensitiveFields = new ArrayList<>();
            sensitiveFields.add("url");
            sensitiveFields.add("cvv");
            sensitiveFields.add("access_token");
        }
        String masked = input;

        for (String field : sensitiveFields) {
            String regex = "\"" + field + "\"\\s*:\\s*\"(.*?)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(masked);

            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String originalValue = matcher.group(1);
                String maskedValue;

                if ("url".equalsIgnoreCase(field)) {
                    maskedValue = maskUrlHost(originalValue);
                } else {
                    maskedValue = maskKeepN(originalValue, 30);
                }

                matcher.appendReplacement(sb, "\"" + field + "\":\"" + maskedValue + "\"");
            }
            matcher.appendTail(sb);
            masked = sb.toString();
        }

        return masked;
    }

    private String maskKeepN(String value, int keep) {
        if (value.length() <= keep) return value;
        return value.substring(0, keep) + "****";
    }

    private String maskUrlHost(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme() != null ? uri.getScheme() + "://" : "";
            String path = uri.getRawPath() != null ? uri.getRawPath() : "";
            String query = uri.getRawQuery() != null ? "?" + uri.getRawQuery() : "";
            return scheme + "****" + path + query;
        } catch (Exception e) {
            return "****";
        }
    }

    public String maskedLastIndex(String input) {
        if (input == null || input.length() <= 4) return input;
        int maskedLength = input.length() - 4;
        String masked = "*".repeat(maskedLength);
        String lastIndex = input.substring(maskedLength);
        return masked + lastIndex;
    }
}
