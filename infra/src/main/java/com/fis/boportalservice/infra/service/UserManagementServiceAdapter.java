package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.UserManagementServicePort;
import com.fis.boportalservice.infra.feignclient.AuthServiceClient;
import com.fis.boportalservice.infra.feignclient.HrmServiceClient;
import com.fis.boportalservice.infra.feignclient.ResponseFeignClient;
import com.fis.boportalservice.infra.feignclient.dto.AuthAssignRoleRequest;
import com.fis.boportalservice.infra.feignclient.dto.AuthIdentityStatusDto;
import com.fis.boportalservice.infra.feignclient.dto.AuthUpdateIdentitySysStatusRequest;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceAdapter implements UserManagementServicePort {

  private final AuthServiceClient authServiceClient;
  private final HrmServiceClient hrmServiceClient;

  @Override
  public UserPageResult filterUsers(UserFilterCriteria criteria, int page, int size) {
    HrmFilterRequest request = toHrmFilterRequest(criteria);
    List<String> normalizedStatuses = normalizeFilters(criteria.sysStatuses());
    List<String> normalizedRoles = normalizeFilters(criteria.roles());

    log.info(
        "event=HRM_FILTER_USERS_REQUEST page={} size={} keyword={} statuses={} roles={} positions={}",
        page,
        size,
        request.getKeyword(),
        normalizedStatuses,
        normalizedRoles,
        request.getPositions()
    );

    if (hasValues(normalizedRoles) || hasValues(normalizedStatuses)) {
      Set<String> allowedUserIds = hasValues(normalizedRoles) ? resolveUserIdsByRoles(normalizedRoles) : null;
      log.info(
          "event=AUTH_FILTER_RESOLVED requestedRoles={} requestedStatuses={} allowedUserCount={}",
          normalizedRoles,
          normalizedStatuses,
          allowedUserIds != null ? allowedUserIds.size() : null
      );
      if (allowedUserIds != null && allowedUserIds.isEmpty()) {
        return new UserPageResult(Collections.emptyList(), 0L, 0);
      }
      return filterUsersByAuthCriteria(request, page, size, allowedUserIds, normalizedStatuses);
    }

    HrmPageResponse<HrmFilterResponse> payload = fetchUsers(request, page, size);
    return toUserPageResult(payload, page, size);
  }

  @Override
  public UserDetail getUserById(Long userId) {
    log.info("event=HRM_USER_DETAIL_REQUEST targetUserId={}", userId);
    HrmUserResponse user = extractData(hrmServiceClient.getUserById(userId));
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
        identityStatus != null ? identityStatus.getSysStatus() : null,
        role != null ? role.getName() : null
    );
    return toDetail(user, role, identityStatus);
  }

  @Override
  public UserMetaOptions getMetaOptions() {
    log.info("event=AUTH_META_ROLES_REQUEST");
    List<AuthzRoleDto> authRoles = Optional.ofNullable(authServiceClient.getRoles())
        .map(this::extractPayload)
        .orElse(Collections.emptyList());

    log.info("event=HRM_META_POSITIONS_REQUEST");
    List<HrmPositionResponse> hrmPositions = getPositions();

    List<String> roles = authRoles.stream()
        .map(AuthzRoleDto::getName)
        .filter(this::hasText)
        .distinct()
        .sorted(String::compareToIgnoreCase)
        .toList();

    if (roles.isEmpty()) {
      roles = hrmPositions.stream()
          .map(HrmPositionResponse::getName)
          .map(this::extractDisplayRole)
          .filter(this::hasText)
          .distinct()
          .sorted(String::compareToIgnoreCase)
          .toList();
    }

    List<String> positions = hrmPositions.stream()
        .map(HrmPositionResponse::getName)
        .map(this::extractDisplayPosition)
        .filter(this::hasText)
        .distinct()
        .sorted(String::compareToIgnoreCase)
        .toList();

    log.info(
        "event=USER_META_COMPUTED authRoles={} rawPositions={} roleOptions={} positionOptions={}",
        authRoles.size(),
        hrmPositions.size(),
        roles.size(),
        positions.size()
    );
    return new UserMetaOptions(roles, positions, Collections.emptyList());
  }

  @Override
  public UserDetail lockUser(Long userId) {
    log.info("event=AUTH_LOCK_REQUEST targetUserId={} sysStatus=SUSPENDED", userId);
    updateAuthIdentitySysStatus(userId, "SUSPENDED");
    return reloadUserDetail(userId);
  }

  @Override
  public UserDetail unlockUser(Long userId) {
    log.info("event=AUTH_UNLOCK_REQUEST targetUserId={} sysStatus=ACTIVE", userId);
    updateAuthIdentitySysStatus(userId, "ACTIVE");
    return reloadUserDetail(userId);
  }

  @Override
  public UserDetail approveUser(Long userId) {
    log.info("event=AUTH_APPROVE_REQUEST targetUserId={} sysStatus=ACTIVE", userId);
    updateAuthIdentitySysStatus(userId, "ACTIVE");
    return reloadUserDetail(userId);
  }

  @Override
  public UserDetail rejectUser(Long userId, String reason) {
    log.info("event=AUTH_REJECT_REQUEST targetUserId={} reason={} sysStatus=INACTIVE", userId, reason);
    updateAuthIdentitySysStatus(userId, "INACTIVE");
    return reloadUserDetail(userId);
  }

  @Override
  public UserDetail suspendUser(Long userId, String reason) {
    log.info("event=AUTH_SUSPEND_REQUEST targetUserId={} reason={} sysStatus=SUSPENDED", userId, reason);
    updateAuthIdentitySysStatus(userId, "SUSPENDED");
    return reloadUserDetail(userId);
  }

  @Override
  public UserDetail reactivateUser(Long userId) {
    log.info("event=AUTH_REACTIVATE_REQUEST targetUserId={} sysStatus=ACTIVE", userId);
    updateAuthIdentitySysStatus(userId, "ACTIVE");
    return reloadUserDetail(userId);
  }

  @Override
  public UserDetail resetPassword(Long userId) {
    log.info("event=AUTH_RESET_PASSWORD_REQUEST targetUserId={}", userId);
    authServiceClient.resetPassword(userId);
    return reloadUserDetail(userId);
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

    HrmUserResponse current = extractData(hrmServiceClient.getUserById(userId));

    if (current == null) {
      log.warn("event=HRM_PROFILE_UPDATE_ABORTED targetUserId={} reason=current-profile-not-found", userId);
      return null;
    }

    hrmServiceClient.updateUserProfile(userId, new HrmUpdateProfileRequest(
        valueOrFallback(command.fullName(), current.getFullName()),
        current.getEmail(),
        current.getDateOfBirth() != null ? current.getDateOfBirth() : LocalDate.of(2000, 1, 1),
        current.getIdNumber(),
        current.getAddress(),
        valueOrFallback(command.phoneNumber(), current.getPhoneNumber()),
        resolvePositionId(command.positionCode(), current.getPositionCode()),
        valueOrFallback(current.getSysStatus(), "APPROVED")
    ));

    log.info("event=HRM_PROFILE_UPDATE_SUCCESS targetUserId={}", userId);
    return reloadUserDetail(userId);
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
        null,
        item.getEmail(),
        item.getRole(),
        item.getPosition(),
        null,
        false
    );
  }

  private UserListItem enrichUserListItem(
      HrmFilterResponse item,
      Map<Long, AuthzRoleDto> roleCache,
      Map<Long, String> identityStatusCache
  ) {
    UserListItem base = toListItem(item);
    if (base == null || base.userId() == null) {
      return base;
    }

    AuthzRoleDto role = getUserRoleCached(base.userId(), roleCache);
    String identityStatus = getIdentityStatusCached(base.userId(), identityStatusCache);

    return new UserListItem(
        base.no(),
        base.userId(),
        base.avatarUrl(),
        base.fullName(),
        identityStatus,
        base.email(),
        role != null && hasText(role.getName()) ? role.getName() : base.role(),
        base.position(),
        base.department(),
        base.deleted()
    );
  }

  private UserListItem applySystemRole(UserListItem item) {
    if (item == null || item.userId() == null) {
      return item;
    }

    AuthzRoleDto role = getUserRole(item.userId());
    if (role == null || role.getName() == null || role.getName().isBlank()) {
      return item;
    }

    return new UserListItem(
        item.no(),
        item.userId(),
        item.avatarUrl(),
        item.fullName(),
        item.sysStatus(),
        item.email(),

        role.getName(),
        item.position(),
        item.department(),
        item.deleted()
    );
  }

  private UserDetail toDetail(HrmUserResponse item, AuthzRoleDto role, AuthIdentityStatusDto identityStatus) {
    String authSysStatus = identityStatus != null ? identityStatus.getSysStatus() : null;
    Long mentorId = item.getMentorId();
    return new UserDetail(
        item.getUserId(),
        item.getEmail(),
        item.getFullName(),
        item.getPhoneNumber(),
        item.getAvatarUrl(),
        item.getPositionCode(),
        role != null ? role.getName() : null,
        authSysStatus,
        authSysStatus,
        item.getDepartment(),
        resolveMentorName(mentorId),
        mentorId,
        "APPROVED".equalsIgnoreCase(item.getSysStatus()),
        false
    );
  }

  private String resolveMentorName(Long mentorId) {
    if (mentorId == null) {
      return null;
    }
    try {
      HrmUserResponse mentor = extractData(hrmServiceClient.getUserById(mentorId));
      return mentor != null ? mentor.getFullName() : null;
    } catch (Exception ex) {
      log.warn("event=HRM_MENTOR_DETAIL_FAILED mentorId={} message={}", mentorId, ex.getMessage());
      return null;
    }
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

    List<HrmPositionResponse> positions = getPositions();

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

  private boolean hasValues(List<String> values) {
    return values != null && !values.isEmpty();
  }

  private Set<String> resolveUserIdsByRoles(List<String> requestedRoles) {
    List<AuthzRoleDto> authRoles = Optional.ofNullable(authServiceClient.getRoles())
        .map(this::extractPayload)
        .orElse(Collections.emptyList());

    Set<String> normalizedRequestedRoles = new LinkedHashSet<>(requestedRoles);
    Set<String> userIds = new LinkedHashSet<>();

    authRoles.stream()
        .filter(role -> role != null && role.getId() != null && role.getName() != null)
        .filter(role -> normalizedRequestedRoles.contains(role.getName().trim().toUpperCase(Locale.ROOT)))
        .forEach(role -> {
          List<String> attachedUsers = Optional.ofNullable(authServiceClient.getUsersByRoleId(String.valueOf(role.getId())))
              .map(this::extractPayload)
              .orElse(Collections.emptyList());
          attachedUsers.stream()
              .filter(this::hasText)
              .forEach(userIds::add);
        });

    return userIds;
  }

  private UserPageResult filterUsersByAuthCriteria(
      HrmFilterRequest request,
      int page,
      int size,
      Set<String> allowedUserIds,
      List<String> requestedStatuses
  ) {
    long startIndex = (long) page * size;
    long endExclusive = startIndex + size;
    long totalFilteredItems = 0L;
    List<HrmFilterResponse> pagedItems = new ArrayList<>();
    int totalPages = 0;
    Map<Long, String> identityStatusCache = new HashMap<>();
    Map<Long, AuthzRoleDto> roleCache = new HashMap<>();

    for (int currentPage = 0; ; currentPage++) {
      HrmPageResponse<HrmFilterResponse> payload = fetchUsers(request, currentPage, size);
      List<HrmFilterResponse> items = getItems(payload);
      totalPages = payload != null ? payload.getTotalPages() : 0;
      identityStatusCache.putAll(loadIdentityStatuses(items));

      for (HrmFilterResponse item : items) {
        if (!matchesRoleFilter(item, allowedUserIds)) {
          continue;
        }
        if (!matchesIdentityStatusFilter(item, requestedStatuses, identityStatusCache)) {
          continue;
        }
        if (totalFilteredItems >= startIndex && totalFilteredItems < endExclusive) {
          pagedItems.add(item);
        }
        totalFilteredItems++;
      }

      if (payload == null || currentPage + 1 >= totalPages) {
        break;
      }
    }

    int filteredTotalPages = size > 0 ? (int) Math.ceil((double) totalFilteredItems / size) : 0;
    log.info(
        "event=HRM_FILTER_USERS_SUCCESS page={} size={} totalItems={} totalPages={} returnedItems={} mode=auth-aware",
        page,
        size,
        totalFilteredItems,
        filteredTotalPages,
        pagedItems.size()
    );

    return new UserPageResult(
        pagedItems.stream()
            .map(item -> enrichUserListItem(item, roleCache, identityStatusCache))
            .toList(),
        totalFilteredItems,
        filteredTotalPages
    );
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
    return hasText(value) ? value : fallback;
  }

  private UserDetail reloadUserDetail(Long userId) {
    return getUserById(userId);
  }

  private HrmFilterRequest toHrmFilterRequest(UserFilterCriteria criteria) {
    HrmFilterRequest request = new HrmFilterRequest();
    request.setKeyword(criteria.keyword());
    request.setSysStatuses(null);
    request.setRoles(null);
    request.setPositions(normalizeFilters(criteria.positions()));
    return request;
  }

  private HrmPageResponse<HrmFilterResponse> fetchUsers(HrmFilterRequest request, int page, int size) {
    return extractData(hrmServiceClient.filterUsers(request, page, size));
  }

  private List<HrmFilterResponse> getItems(HrmPageResponse<HrmFilterResponse> payload) {
    return payload != null && payload.getItems() != null ? payload.getItems() : Collections.emptyList();
  }

  private UserPageResult toUserPageResult(HrmPageResponse<HrmFilterResponse> payload, int page, int size) {
    List<HrmFilterResponse> items = getItems(payload);
    Map<Long, AuthzRoleDto> roleCache = new HashMap<>();
    Map<Long, String> identityStatusCache = loadIdentityStatuses(items);

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
            .map(item -> enrichUserListItem(item, roleCache, identityStatusCache))
            .toList(),
        payload != null ? payload.getTotalItems() : 0L,
        payload != null ? payload.getTotalPages() : 0
    );
  }

  private List<HrmPositionResponse> getPositions() {
    return defaultIfNull(extractData(hrmServiceClient.getPositions()));
  }

  private AuthzRoleDto getUserRole(Long userId) {
    return extractPayload(authServiceClient.getRoleByUserId(userId));
  }

  private void updateAuthIdentitySysStatus(Long userId, String sysStatus) {
    authServiceClient.updateIdentitySysStatus(userId, new AuthUpdateIdentitySysStatusRequest(sysStatus));
  }

  private AuthzRoleDto getUserRoleCached(Long userId, Map<Long, AuthzRoleDto> roleCache) {
    if (userId == null) {
      return null;
    }
    return roleCache.computeIfAbsent(userId, this::getUserRole);
  }

  private String getIdentityStatus(Long userId) {
    AuthIdentityStatusDto dto = extractPayload(authServiceClient.getIdentityStatus(userId));
    return dto != null ? dto.getSysStatus() : null;
  }

  private String getIdentityStatusCached(Long userId, Map<Long, String> identityStatusCache) {
    if (userId == null) {
      return null;
    }
    return identityStatusCache.computeIfAbsent(userId, this::getIdentityStatus);
  }

  private boolean matchesRoleFilter(HrmFilterResponse item, Set<String> allowedUserIds) {
    if (allowedUserIds == null) {
      return true;
    }
    return item != null && item.getUserId() != null && allowedUserIds.contains(String.valueOf(item.getUserId()));
  }

  private boolean matchesIdentityStatusFilter(
      HrmFilterResponse item,
      List<String> requestedStatuses,
      Map<Long, String> identityStatusCache
  ) {
    if (!hasValues(requestedStatuses)) {
      return true;
    }
    if (item == null || item.getUserId() == null) {
      return false;
    }

    String identityStatus = getIdentityStatusCached(item.getUserId(), identityStatusCache);
    return hasText(identityStatus) && requestedStatuses.contains(identityStatus.trim().toUpperCase(Locale.ROOT));
  }

  private Map<Long, String> loadIdentityStatuses(List<HrmFilterResponse> items) {
    List<Long> userIds = items.stream()
        .map(HrmFilterResponse::getUserId)
        .filter(Objects::nonNull)
        .distinct()
        .toList();

    if (userIds.isEmpty()) {
      return new HashMap<>();
    }

    List<AuthIdentityStatusDto> statuses = Optional.ofNullable(authServiceClient.getIdentityStatuses(userIds))
        .map(this::extractPayload)
        .orElse(Collections.emptyList());

    Map<Long, String> statusMap = new HashMap<>();
    for (AuthIdentityStatusDto status : statuses) {
      if (status == null || status.getUserId() == null || !hasText(status.getSysStatus())) {
        continue;
      }
      statusMap.put(status.getUserId(), status.getSysStatus());
    }
    return statusMap;
  }

  private <T> T extractData(ResponseApi<T> response) {
    return response != null ? response.data() : null;
  }

  private <T> List<T> defaultIfNull(List<T> values) {
    return values != null ? values : Collections.emptyList();
  }

  private boolean hasText(String value) {
    return value != null && !value.isBlank();
  }

  private <T> T extractPayload(ResponseFeignClient<T> response) {
    if (response == null) {
      return null;
    }
    return response.getData() != null ? response.getData() : response.getResult();
  }
}
