package com.fis.boportalservice.api.configuration;

import com.fis.boportalservice.api.configuration.xss.XSSFilter;
import com.fis.boportalservice.core.util.RequestIdMdcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterBeanConfiguration {
    @Bean
    public FilterRegistrationBean<RequestIdMdcFilter> requestHeaderMdcFilter() {
        FilterRegistrationBean<RequestIdMdcFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestIdMdcFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<XSSFilter> xssFilterRegistration() {
        FilterRegistrationBean<XSSFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XSSFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
