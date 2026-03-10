package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.infra.feignclient.dto.HrmUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hrm-service", url = "${feign.client.config.hrm-service.url:http://hrm-service:8080}")
public interface HrmServiceClient {

  @GetMapping("/hrm/internal/users/{userId}")
  ResponseApi<HrmUserResponse> getUserById(@PathVariable("userId") Long userId);
}
