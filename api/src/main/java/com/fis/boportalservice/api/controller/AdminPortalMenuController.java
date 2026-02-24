package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.PortalMenuRequest;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.api.mapper.PortalMenuApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.core.service.PortalMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.fis.boportalservice.api.util.MenuHierarchyHelper;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/menus")
@RequiredArgsConstructor
public class AdminPortalMenuController {

    private final PortalMenuService menuService;
    private final PortalMenuApiMapper apiMapper;

    @GetMapping
    public ResponseApi<List<PortalMenuResponse>> getAllMenus() {
        log.info("Request to get all portal menus (flat list)");
        List<PortalMenuResponse> flatMenus = apiMapper.toResponseList(menuService.getAllMenus());
        return ResponseApi.success(MenuHierarchyHelper.buildMenuHierarchy(flatMenus));
    }

    @GetMapping("/{id}")
    public ResponseApi<PortalMenuResponse> getMenuById(@PathVariable Integer id) {
        log.info("Request to get portal menu by id: {}", id);
        return ResponseApi.success(apiMapper.toResponse(menuService.getMenuById(id)));
    }

    @PostMapping
    public ResponseApi<PortalMenuResponse> createMenu(@Valid @RequestBody PortalMenuRequest request) {
        log.info("Request to create portal menu: {}", request);
        PortalMenu domain = apiMapper.toDomain(request);
        return ResponseApi.success(apiMapper.toResponse(menuService.createMenu(domain)));
    }

    @PutMapping("/{id}")
    public ResponseApi<PortalMenuResponse> updateMenu(@PathVariable Integer id,
            @Valid @RequestBody PortalMenuRequest request) {
        log.info("Request to update portal menu: id={}, request={}", id, request);
        PortalMenu domain = apiMapper.toDomain(request);
        return ResponseApi.success(apiMapper.toResponse(menuService.updateMenu(id, domain)));
    }

    @DeleteMapping("/{id}")
    public ResponseApi<Void> deleteMenu(@PathVariable Integer id) {
        log.info("Request to delete portal menu: {}", id);
        menuService.deleteMenu(id);
        return ResponseApi.success(null);
    }
}
