package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.UserManagementServicePort;
import com.fis.boportalservice.infra.feignclient.AuthServiceClient;
import com.fis.boportalservice.infra.feignclient.HrmServiceClient;
import com.fis.boportalservice.infra.feignclient.ResponseFeignClient;
import com.fis.boportalservice.infra.feignclient.dto.AuthAssignRoleRequest;
import com.fis.boportalservice.infra.feignclient.dto.AuthIdentityStatusDto;
import com.fis.boportalservice.infra.feignclient.dto.AuthzRoleDto;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmPageResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmPositionResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmUpdateProfileRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
    request.setRoles(normalizeFilters(criteria.roles()));
    request.setPositions(normalizeFilters(criteria.positions()));

    log.info(
        "event=HRM_FILTER_USERS_REQUEST page={} size={} keyword={} statuses={} roles={} positions={}",
        page,
        size,
        request.getKeyword(),
        request.getSysStatuses(),
        request.getRoles(),
        request.getPositions()
    );

    ResponseApi<HrmPageResponse<HrmFilterResponse>> response = hrmServiceClient.filterUsers(request, page, size);
    HrmPageResponse<HrmFilterResponse> payload = response != null ? response.data() : null;
    List<HrmFilterResponse> items = payload != null && payload.getItems() != null
        ? payload.getItems()
        : Collections.emptyList();

    log.info(
        "event=HRM_FILTER_USERS_SUCCESS page={} size={} totalItems={} totalPages={} returnedItems={}",
        page,
        size,
        payload != null ? payload.getTotalItems() : 0,
        payload != null ? payload.getTotalPages() : 0,
        items.size()
    );

    return new UserPageResult(
        items.stream()
            .map(this::toListItem)
            .map(this::applySystemRole)
            .toList(),
        payload != null ? payload.getTotalItems() : 0L,
        payload != null ? payload.getTotalPages() : 0
    );
  }

  @Override
  public UserDetail getUserById(Long userId) {
    log.info("event=HRM_USER_DETAIL_REQUEST targetUserId={}", userId);
    HrmUserResponse user = Optional.ofNullable(hrmServiceClient.getUserById(userId))
        .map(ResponseApi::data)
        .orElse(null);
    if (user == null) {
      log.warn("event=HRM_USER_DETAIL_EMPTY targetUserId={}", userId);
      return null;
    }

    log.info("event=AUTH_ROLE_LOOKUP_REQUEST targetUserId={}", userId);
    AuthzRoleDto role = extractPayload(authServiceClient.getRoleByUserId(userId));
    log.info("event=AUTH_IDENTITY_STATUS_REQUEST targetUserId={}", userId);
    AuthIdentityStatusDto identityStatus = extractPayload(authServiceClient.getIdentityStatus(userId));

    log.info(
        "event=USER_DETAIL_COMPOSED targetUserId={} businessStatus={} loginStatus={} role={}",
        userId,
        user.getSysStatus(),
        identityStatus != null ? identityStatus.getStatus() : null,
        role != null ? role.getName() : null
    );
    return toDetail(user, role, identityStatus);
  }

  @Override
  public UserMetaOptions getMetaOptions() {
    log.info("event=HRM_META_POSITIONS_REQUEST");
    List<HrmPositionResponse> hrmPositions = Optional.ofNullable(hrmServiceClient.getPositions())
        .map(ResponseApi::data)
        .orElse(Collections.emptyList());

    List<String> roles = hrmPositions.stream()
        .map(HrmPositionResponse::getName)
        .map(this::extractDisplayRole)
        .filter(name -> name != null && !name.isBlank())
        .distinct()
        .sorted(String::compareToIgnoreCase)
        .toList();

    List<String> positions = hrmPositions.stream()
        .map(HrmPositionResponse::getName)
        .map(this::extractDisplayPosition)
        .filter(name -> name != null && !name.isBlank())
        .distinct()
        .sorted(String::compareToIgnoreCase)
        .toList();

    log.info(
        "event=USER_META_COMPUTED rawPositions={} roleOptions={} positionOptions={}",
        hrmPositions.size(),
        roles.size(),
        positions.size()
    );
    return new UserMetaOptions(roles, positions, Collections.emptyList());
  }

  @Override
  public UserDetail lockUser(Long userId) {
    log.info("event=AUTH_LOCK_REQUEST targetUserId={}", userId);
    authServiceClient.lockIdentity(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail unlockUser(Long userId) {
    log.info("event=AUTH_UNLOCK_REQUEST targetUserId={}", userId);
    authServiceClient.unlockIdentity(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail approveUser(Long userId) {
    log.info("event=HRM_APPROVE_REQUEST targetUserId={}", userId);
    hrmServiceClient.approveUser(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail rejectUser(Long userId, String reason) {
    log.info("event=HRM_REJECT_REQUEST targetUserId={} reason={}", userId, reason);
    hrmServiceClient.rejectUser(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail suspendUser(Long userId, String reason) {
    log.info("event=HRM_SUSPEND_REQUEST targetUserId={} reason={}", userId, reason);
    hrmServiceClient.suspendUser(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail reactivateUser(Long userId) {
    log.info("event=HRM_REACTIVATE_REQUEST targetUserId={}", userId);
    hrmServiceClient.unlockUser(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail resetPassword(Long userId) {
    log.info("event=AUTH_RESET_PASSWORD_REQUEST targetUserId={}", userId);
    authServiceClient.resetPassword(userId);
    return getUserById(userId);
  }

  @Override
  public UserDetail updateProfile(Long userId, UserProfileUpdateCommand command) {
    log.info(
        "event=HRM_PROFILE_UPDATE_REQUEST targetUserId={} fullName={} phoneNumber={} positionCode={} department={}",
        userId,
        command.fullName(),
        command.phoneNumber(),
        command.positionCode(),
        command.department()
    );

    HrmUserResponse current = Optional.ofNullable(hrmServiceClient.getUserById(userId))
        .map(ResponseApi::data)
        .orElse(null);

    if (current == null) {
      log.warn("event=HRM_PROFILE_UPDATE_ABORTED targetUserId={} reason=current-profile-not-found", userId);
      return null;
    }

    hrmServiceClient.updateUserProfile(userId, new HrmUpdateProfileRequest(
        valueOrFallback(command.fullName(), current.getFullName()),
        current.getEmail(),
        current.getDateOfBirth() != null ? current.getDateOfBirth() : LocalDate.of(2000, 1, 1),
        valueOrFallback(current.getIdNumber(), current.getIdNumber()),
        valueOrFallback(current.getAddress(), current.getAddress()),
        valueOrFallback(command.phoneNumber(), current.getPhoneNumber()),
        resolvePositionId(command.positionCode(), current.getPositionCode()),
        valueOrFallback(current.getSysStatus(), "APPROVED")
    ));

    log.info("event=HRM_PROFILE_UPDATE_SUCCESS targetUserId={}", userId);
    return getUserById(userId);
  }

  @Override
  public UserRoleResult getUserRoles(Long userId) {
    log.info("event=AUTH_ROLE_READ_REQUEST targetUserId={}", userId);
    AuthzRoleDto role = extractPayload(authServiceClient.getRoleByUserId(userId));
    List<UserRole> roles = role == null ? Collections.emptyList() : List.of(toUserRole(role));
    log.info("event=AUTH_ROLE_READ_SUCCESS targetUserId={} roleCount={}", userId, roles.size());
    return new UserRoleResult(userId, roles);
  }

  @Override
  public UserRoleResult assignRole(Long userId, String roleId) {
    log.info("event=AUTH_ROLE_ASSIGN_REQUEST targetUserId={} roleId={}", userId, roleId);
    if (roleId != null && !roleId.isBlank()) {
      authServiceClient.assignRoleToUser(userId, new AuthAssignRoleRequest(Long.parseLong(roleId)));
    }
    log.info("event=AUTH_ROLE_ASSIGN_SUCCESS targetUserId={} roleId={}", userId, roleId);
    return getUserRoles(userId);
  }

  @Override
  public List<UserHistoryItem> getActivityHistory(Long userId) {
    log.info("event=USER_ACTIVITY_HISTORY_EMPTY targetUserId={} source=audit-service-not-ready", userId);
    return Collections.emptyList();
  }

  @Override
  public List<UserHistoryItem> getLoginHistory(Long userId) {
    log.info("event=USER_LOGIN_HISTORY_EMPTY targetUserId={} source=audit-service-not-ready", userId);
    return Collections.emptyList();
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
        item.getPosition(),
        null,
        false
    );
  }

  private UserListItem applySystemRole(UserListItem item) {
    if (item == null || item.userId() == null) {
      return item;
    }

    AuthzRoleDto role = extractPayload(authServiceClient.getRoleByUserId(item.userId()));
    if (role == null || role.getName() == null || role.getName().isBlank()) {
      return item;
    }

    return new UserListItem(
        item.no(),
        item.userId(),
        item.avatarUrl(),
        item.fullName(),
        item.sysStatus(),
        item.email(),ok

        role.getName(),
        item.position(),
        item.department(),
        item.deleted()
    );
  }

  private UserDetail toDetail(HrmUserResponse item, AuthzRoleDto role, AuthIdentityStatusDto identityStatus) {
    return new UserDetail(
        item.getUserId(),
        item.getEmail(),
        item.getFullName(),
        item.getPhoneNumber(),
        item.getAvatarUrl(),
        item.getPositionCode(),
        role != null ? role.getName() : null,
        item.getSysStatus(),
        identityStatus != null ? identityStatus.getStatus() : null,
        item.getDepartment(),
        "APPROVED".equalsIgnoreCase(item.getSysStatus()),
        false
    );
  }

  private UserRole toUserRole(AuthzRoleDto role) {
    return new UserRole(
        role.getId(),
        role.getName(),
        role.getName(),
        role.getDescription()
    );
  }

  private Long resolvePositionId(String requestedPosition, String fallbackPosition) {
    String target = requestedPosition != null && !requestedPosition.isBlank() ? requestedPosition : fallbackPosition;
    if (target == null || target.isBlank()) {
      return null;
    }

    List<HrmPositionResponse> positions = Optional.ofNullable(hrmServiceClient.getPositions())
        .map(ResponseApi::data)
        .orElse(Collections.emptyList());

    try {
      long parsedId = Long.parseLong(target);
      return positions.stream()
          .filter(position -> Objects.equals(position.getPositionId(), parsedId))
          .map(HrmPositionResponse::getPositionId)
          .findFirst()
          .orElse(parsedId);
    } catch (NumberFormatException ignored) {
      return positions.stream()
          .filter(position -> target.equalsIgnoreCase(position.getName()))
          .map(HrmPositionResponse::getPositionId)
          .findFirst()
          .orElse(null);
    }
  }

  private List<String> normalizeFilters(List<String> values) {
    if (values == null) {
      return null;
    }
    return values.stream()
        .filter(value -> value != null && !value.isBlank())
        .map(value -> value.trim().toUpperCase(Locale.ROOT))
        .toList();
  }

  private String extractDisplayRole(String rawName) {
    if (rawName == null || rawName.isBlank()) {
      return null;
    }

    String normalized = rawName.trim();
    String[] parts = normalized.split("\\s+");
    if (parts.length >= 2) {
      return parts[0].toUpperCase(Locale.ROOT);
    }

    String upperName = normalized.toUpperCase(Locale.ROOT);
    if (upperName.startsWith("INTERN") && upperName.length() > 6) {
      return "INTERN";
    }
    if (upperName.startsWith("STAFF") && upperName.length() > 5) {
      return "STAFF";
    }

    return upperName;
  }

  private String extractDisplayPosition(String rawName) {
    if (rawName == null || rawName.isBlank()) {
      return null;
    }

    String normalized = rawName.trim();
    String[] parts = normalized.split("\\s+");
    if (parts.length >= 2) {
      return String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length)).toUpperCase(Locale.ROOT);
    }

    String upperName = normalized.toUpperCase(Locale.ROOT);
    if (upperName.startsWith("INTERN") && upperName.length() > 6) {
      return upperName.substring(6).trim();
    }
    if (upperName.startsWith("STAFF") && upperName.length() > 5) {
      return upperName.substring(5).trim();
    }

    return null;
  }

  private String valueOrFallback(String value, String fallback) {
    return value != null && !value.isBlank() ? value : fallback;
  }

  private <T> T extractPayload(ResponseFeignClient<T> response) {
    if (response == null) {
      return null;
    }
    return response.getData() != null ? response.getData() : response.getResult();
  }
}
