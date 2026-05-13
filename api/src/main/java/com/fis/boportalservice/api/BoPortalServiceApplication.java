package com.fis.boportalservice.api;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableFeignClients(basePackages = "com.fis.boportalservice.infra")
@SpringBootApplication(scanBasePackages = {"com.fis.boportalservice"})
@EnableJpaRepositories(basePackages = "com.fis.boportalservice.infra")
@EntityScan(basePackages = "com.fis.boportalservice.infra")
@ConfigurationPropertiesScan("com.fis.boportalservice")
@EnableAsync
@RequiredArgsConstructor
public class BoPortalServiceApplication {

  static void main(String[] args) {
    SpringApplication.run(BoPortalServiceApplication.class, args);
  }

}
