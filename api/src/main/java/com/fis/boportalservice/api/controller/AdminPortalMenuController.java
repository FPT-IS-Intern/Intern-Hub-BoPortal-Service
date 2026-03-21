package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.PortalMenuRequest;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.api.mapper.PortalMenuApiMapper;
import com.fis.boportalservice.api.util.MenuHierarchyHelper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.core.service.PortalMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "4. Admin - Portal Menus")
@RestController
@RequestMapping("/bo-portal/menus")
@RequiredArgsConstructor
public class AdminPortalMenuController {

  private final PortalMenuService menuService;
  private final PortalMenuApiMapper apiMapper;

  @GetMapping
  public ResponseApi<List<PortalMenuResponse>> getAllMenus() {
    log.info("event=PORTAL_MENU_LIST_REQUEST");
    List<PortalMenuResponse> flatMenus = apiMapper.toResponseList(menuService.getAllMenus());
    return ResponseApi.success(MenuHierarchyHelper.buildMenuHierarchy(flatMenus));
  }

  @GetMapping("/{id}")
  public ResponseApi<PortalMenuResponse> getMenuById(@PathVariable Integer id) {
    log.info("event=PORTAL_MENU_DETAIL_REQUEST id={}", id);
    return ResponseApi.success(apiMapper.toResponse(menuService.getMenuById(id)));
  }

  @PostMapping
  public ResponseApi<PortalMenuResponse> createMenu(@Valid @RequestBody PortalMenuRequest request) {
    log.info("event=PORTAL_MENU_CREATE_REQUEST request={}", request);
    PortalMenu domain = apiMapper.toDomain(request);
    return ResponseApi.success(apiMapper.toResponse(menuService.createMenu(domain)));
  }

  @PutMapping("/{id}")
  public ResponseApi<PortalMenuResponse> updateMenu(@PathVariable Integer id,
                                                    @Valid @RequestBody PortalMenuRequest request) {
    log.info("event=PORTAL_MENU_UPDATE_REQUEST id={} request={}", id, request);
    PortalMenu domain = apiMapper.toDomain(request);
    return ResponseApi.success(apiMapper.toResponse(menuService.updateMenu(id, domain)));
  }

  @DeleteMapping("/{id}")
  public ResponseApi<Void> deleteMenu(@PathVariable Integer id) {
    log.info("event=PORTAL_MENU_DELETE_REQUEST id={}", id);
    menuService.deleteMenu(id);
    return ResponseApi.success(null);
  }
}

