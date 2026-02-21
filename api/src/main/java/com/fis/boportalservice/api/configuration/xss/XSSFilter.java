package com.fis.boportalservice.api.configuration.xss;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XSSFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;

            String contentType = req.getContentType();
            if (contentType != null && contentType.contains("application/json")) {
                String body = getBody(req);
                String sanitizedBody = sanitizeJson(body);
                CachedBodyHttpServletRequest wrappedRequest =
                        new CachedBodyHttpServletRequest(req, sanitizedBody);
                chain.doFilter(wrappedRequest, response);
            } else {
                chain.doFilter(new XSSRequestWrapper(req), response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    // Đọc body từ request
    private String getBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        char[] buffer = new char[1024];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, bytesRead);
        }
        return stringBuilder.toString();
    }

    // Sanitize từng trường trong JSON object (chỉ trường kiểu String)
    private String sanitizeJson(String json) {
        if (json == null || json.isEmpty()) return json;
        try {
            Map<String, Object> map =
                    objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> sanitizedMap =
                    map.entrySet().stream()
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            e ->
                                                    (e.getValue() instanceof String)
                                                            ? Jsoup.clean((String) e.getValue(), Safelist.none())
                                                            : e.getValue()));
            return objectMapper.writeValueAsString(sanitizedMap);
        } catch (Exception ex) {
            return json;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
