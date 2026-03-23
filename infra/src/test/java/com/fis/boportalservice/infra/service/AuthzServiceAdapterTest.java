package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.core.domain.model.AuthzResource;
import com.fis.boportalservice.core.domain.model.AuthzRole;
import com.fis.boportalservice.infra.feignclient.AuthServiceClient;
import com.fis.boportalservice.infra.feignclient.ResponseFeignClient;
import com.fis.boportalservice.infra.feignclient.dto.AuthzResourceDto;
import com.fis.boportalservice.infra.feignclient.dto.AuthzRoleDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthzServiceAdapterTest {

  private final AuthServiceClient authServiceClient = mock(AuthServiceClient.class);
  private final AuthzServiceAdapter adapter = new AuthzServiceAdapter(authServiceClient);

  @Test
  void getRoles_shouldUseDataPayload_whenPresent() {
    ResponseFeignClient<List<AuthzRoleDto>> response = new ResponseFeignClient<>();
    AuthzRoleDto dto = new AuthzRoleDto();
    dto.setId("1");
    dto.setName("ADMIN");
    dto.setDescription("Administrator");
    dto.setStatus("ACTIVE");
    response.setData(List.of(dto));
    when(authServiceClient.getRoles()).thenReturn(response);

    List<AuthzRole> roles = adapter.getRoles();

    assertEquals(1, roles.size());
    assertEquals("1", roles.get(0).getId());
    assertEquals("ADMIN", roles.get(0).getName());
  }

  @Test
  void getAllResources_shouldFallbackToResultPayload_whenDataIsMissing() {
    ResponseFeignClient<List<AuthzResourceDto>> response = new ResponseFeignClient<>();
    AuthzResourceDto dto = new AuthzResourceDto();
    dto.setId("r-1");
    dto.setName("User");
    dto.setCode("USER");
    dto.setDescription("User resource");
    dto.setCategoryId("cat-1");
    response.setResult(List.of(dto));
    when(authServiceClient.getAllResources()).thenReturn(response);

    List<AuthzResource> resources = adapter.getAllResources();

    assertEquals(1, resources.size());
    assertEquals("r-1", resources.get(0).getId());
    assertEquals("USER", resources.get(0).getCode());
  }

  @Test
  void createRole_shouldReturnNull_whenAuthServiceReturnsNoPayload() {
    ResponseFeignClient<AuthzRoleDto> response = new ResponseFeignClient<>();
    when(authServiceClient.createRole(any())).thenReturn(response);

    AuthzRole role = adapter.createRole("ADMIN", "Administrator");

    assertNull(role);
  }
}
