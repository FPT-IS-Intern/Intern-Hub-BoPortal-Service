package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.PortalMenuRequest;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.api.mapper.PortalMenuApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.core.service.PortalMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/menus")
@RequiredArgsConstructor
public class AdminPortalMenuController {

    private final PortalMenuService menuService;
    private final PortalMenuApiMapper apiMapper;

    @GetMapping
    public ResponseApi<List<PortalMenuResponse>> getAllMenus() {
        return ResponseApi.success(apiMapper.toResponseList(menuService.getAllMenus()));
    }

    @GetMapping("/{id}")
    public ResponseApi<PortalMenuResponse> getMenuById(@PathVariable Integer id) {
        return ResponseApi.success(apiMapper.toResponse(menuService.getMenuById(id)));
    }

    @PostMapping
    public ResponseApi<PortalMenuResponse> createMenu(@Valid @RequestBody PortalMenuRequest request) {
        PortalMenu domain = apiMapper.toDomain(request);
        return ResponseApi.success(apiMapper.toResponse(menuService.createMenu(domain)));
    }

    @PutMapping("/{id}")
    public ResponseApi<PortalMenuResponse> updateMenu(@PathVariable Integer id,
            @Valid @RequestBody PortalMenuRequest request) {
        PortalMenu domain = apiMapper.toDomain(request);
        return ResponseApi.success(apiMapper.toResponse(menuService.updateMenu(id, domain)));
    }

    @DeleteMapping("/{id}")
    public ResponseApi<Void> deleteMenu(@PathVariable Integer id) {
        menuService.deleteMenu(id);
        return ResponseApi.success(null);
    }
}
