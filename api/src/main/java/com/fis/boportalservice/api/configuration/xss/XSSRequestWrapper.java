package com.fis.boportalservice.api.configuration.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return sanitize(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) return null;

        String[] sanitizedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            sanitizedValues[i] = sanitize(values[i]);
        }
        return sanitizedValues;
    }

    @Override
    public String getHeader(String name) {
        return sanitize(super.getHeader(name));
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> headers = super.getHeaders(name);
        return new Enumeration<String>() {
            public boolean hasMoreElements() {
                return headers.hasMoreElements();
            }

            public String nextElement() {
                return sanitize(headers.nextElement());
            }
        };
    }

    @Override
    public String getQueryString() {
        return sanitize(super.getQueryString());
    }

    private String sanitize(String value) {
        if (value == null) return null;
        String cleaned = Jsoup.clean(value, Safelist.none());
        cleaned = cleaned.replaceAll("[\"<>]", "");
        return cleaned;
    }
}
