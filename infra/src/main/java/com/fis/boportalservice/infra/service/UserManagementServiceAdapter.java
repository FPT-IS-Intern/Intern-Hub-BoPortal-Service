package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.UserManagementServicePort;
import com.fis.boportalservice.infra.feignclient.AuthServiceClient;
import com.fis.boportalservice.infra.feignclient.HrmServiceClient;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmPageResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceAdapter implements UserManagementServicePort {

  private final AuthServiceClient authServiceClient;
  private final HrmServiceClient hrmServiceClient;

  @Override
  public UserPageResult filterUsers(UserFilterCriteria criteria, int page, int size) {
    HrmFilterRequest request = new HrmFilterRequest();
    request.setKeyword(criteria.keyword());
    request.setSysStatuses(criteria.sysStatuses());
    request.setRoles(criteria.roles());
    request.setPositions(criteria.positions());

    ResponseApi<HrmPageResponse<HrmFilterResponse>> response = hrmServiceClient.filterUsers(request, page, size);
    HrmPageResponse<HrmFilterResponse> payload = response != null ? response.data() : null;
    List<HrmFilterResponse> items = payload != null && payload.getItems() != null
        ? payload.getItems()
        : Collections.emptyList();

    return new UserPageResult(
        items.stream().map(this::toListItem).toList(),
        payload != null ? payload.getTotalItems() : 0L,
        payload != null ? payload.getTotalPages() : 0
    );
  }

  @Override
  public UserDetail getUserById(Long userId) {
    ResponseApi<HrmUserResponse> response = hrmServiceClient.getUserById(userId);
    return Optional.ofNullable(response)
        .map(ResponseApi::data)
        .map(this::toDetail)
        .orElse(null);
  }

  @Override
  public UserDetail lockUser(Long userId) {
    authServiceClient.lockIdentity(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail unlockUser(Long userId) {
    authServiceClient.unlockIdentity(userId);
    return getUserById(userId);
  }

  private UserListItem toListItem(HrmFilterResponse item) {
    return new UserListItem(
        item.getNo(),
        item.getUserId(),
        item.getAvatarUrl(),
        item.getFullName(),
        item.getSysStatus(),
        item.getEmail(),
        item.getRole(),
        item.getPosition()
    );
  }

  private UserDetail toDetail(HrmUserResponse item) {
    return new UserDetail(
        item.getUserId(),
        item.getEmail(),
        item.getFullName(),
        item.getPhoneNumber(),
        item.getPositionCode(),
        item.getRole(),
        item.getStatus()
    );
  }
}
