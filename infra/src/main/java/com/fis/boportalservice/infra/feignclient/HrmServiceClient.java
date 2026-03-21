package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.infra.configuration.FeignClientCommonConfiguration;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmPageResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmPositionResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmUpdateProfileRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(
    name = "hrm-service",
    url = "${feign.client.config.hrm-service.url:http://hrm-service:8080}",
    configuration = FeignClientCommonConfiguration.class
)
public interface HrmServiceClient {

  @GetMapping("/hrm/internal/users/{userId}")
  ResponseApi<HrmUserResponse> getUserById(@PathVariable("userId") Long userId);

  @GetMapping("/hrm/users/admin/profile/{userId}")
  ResponseApi<HrmUserResponse> getUserAdminProfile(@PathVariable("userId") Long userId);

  @GetMapping("/hrm/internal/positions")
  ResponseApi<java.util.List<HrmPositionResponse>> getPositions();

  @PostMapping("/hrm/internal/users/internal/filter")
  ResponseApi<HrmPageResponse<HrmFilterResponse>> filterUsers(
      @RequestBody HrmFilterRequest request,
      @RequestParam("page") int page,
      @RequestParam("size") int size
  );

  @PutMapping("/hrm/internal/users/{userId}/lock")
  ResponseApi<HrmUserResponse> lockUser(@PathVariable("userId") Long userId);

  @PutMapping("/hrm/internal/users/{userId}/unlock")
  ResponseApi<HrmUserResponse> unlockUser(@PathVariable("userId") Long userId);

  @PutMapping("/hrm/users/approval/{userId}")
  ResponseApi<Object> approveUser(@PathVariable("userId") Long userId);

  @PutMapping("/hrm/users/rejection/{userId}")
  ResponseApi<Object> rejectUser(@PathVariable("userId") Long userId);

  @PutMapping("/hrm/users/suspension/{userId}")
  ResponseApi<HrmUserResponse> suspendUser(@PathVariable("userId") Long userId);

  @PatchMapping(value = "/hrm/users/profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseApi<Object> updateUserProfile(@PathVariable("userId") Long userId, @RequestPart("userInfo") HrmUpdateProfileRequest request);
}
