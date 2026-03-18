package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.infra.configuration.FeignClientCommonConfiguration;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmUserResponse;
import com.intern.hub.library.common.dto.PaginatedData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "hrm-service",
    url = "${feign.client.config.hrm-service.url:http://hrm-service:8080}",
    configuration = FeignClientCommonConfiguration.class
)
public interface HrmServiceClient {

  @GetMapping("/hrm/internal/users/{userId}")
  ResponseApi<HrmUserResponse> getUserById(@PathVariable("userId") Long userId);

  @PostMapping("/hrm/internal/users/internal/filter")
  ResponseApi<PaginatedData<HrmFilterResponse>> filterUsers(
      @RequestBody HrmFilterRequest request,
      @RequestParam("page") int page,
      @RequestParam("size") int size
  );

  @PutMapping("/hrm/internal/users/{userId}/lock")
  ResponseApi<HrmUserResponse> lockUser(@PathVariable("userId") Long userId);

  @PutMapping("/hrm/internal/users/{userId}/unlock")
  ResponseApi<HrmUserResponse> unlockUser(@PathVariable("userId") Long userId);
}
