package com.fis.boportalservice.api;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Slf4j
@EnableFeignClients(basePackages = "com.fis.boportalservice.infra")
@SpringBootApplication(scanBasePackages = {"com.fis.boportalservice"})
@EnableJpaRepositories(basePackages = "com.fis.boportalservice.infra")
@EntityScan(basePackages = "com.fis.boportalservice.infra")
@ConfigurationPropertiesScan("com.fis.boportalservice")
@EnableAsync
@RequiredArgsConstructor
public class BoPortalServiceApplication {

  private final Environment env;

  public static void main(String[] args) {
    SpringApplication.run(BoPortalServiceApplication.class, args);
  }

  @PostConstruct
  public void setUp() {
    String timezoneId = env.getProperty("configuration.timezone");
    if (timezoneId == null || timezoneId.isBlank()) {
      log.warn("event=TIMEZONE_CONFIG_MISSING fallback={}", TimeZone.getDefault().getID());
      return;
    }

    TimeZone timezone = TimeZone.getTimeZone(timezoneId);
    TimeZone.setDefault(timezone);
    log.info("event=TIMEZONE_CONFIG_APPLIED timezone={}", timezone.getID());
  }
}
