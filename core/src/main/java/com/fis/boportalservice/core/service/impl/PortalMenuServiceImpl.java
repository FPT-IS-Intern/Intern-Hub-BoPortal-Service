package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.core.domain.repository.PortalMenuRepository;
import com.fis.boportalservice.core.service.PortalMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortalMenuServiceImpl implements PortalMenuService {

  private final PortalMenuRepository menuRepository;

  @Override
  public List<PortalMenu> getAvailableMenus(List<String> userPermissions) {
    List<PortalMenu> activeMenus = menuRepository.findActiveMenus();

    if (userPermissions == null || userPermissions.isEmpty()) {
      return activeMenus.stream()
          .filter(m -> !StringUtils.hasText(m.getPermissionCode()))
          .collect(Collectors.toList());
    }

    return activeMenus.stream()
        .filter(m -> !StringUtils.hasText(m.getPermissionCode())
            || userPermissions.contains(m.getPermissionCode()))
        .collect(Collectors.toList());
  }

  @Override
  public List<PortalMenu> getAllMenus() {
    return menuRepository.findAll();
  }

  @Override
  public PortalMenu getMenuById(Integer id) {
    return menuRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Portal menu not found with id: {}", id);
          return new RuntimeException("Menu not found with id: " + id);
        });
  }

  @Override
  public PortalMenu createMenu(PortalMenu menu) {
    log.info("Creating new portal menu: {}", menu.getCode());
    return menuRepository.save(menu);
  }

  @Override
  public PortalMenu updateMenu(Integer id, PortalMenu menu) {
    log.info("Updating portal menu with id: {}", id);
    PortalMenu existing = getMenuById(id);

    existing.setCode(menu.getCode());
    existing.setTitle(menu.getTitle());
    existing.setPath(menu.getPath());
    existing.setIcon(menu.getIcon());
    existing.setParentId(menu.getParentId());
    existing.setPermissionCode(menu.getPermissionCode());
    existing.setSortOrder(menu.getSortOrder());
    existing.setStatus(menu.getStatus());

    return menuRepository.save(existing);
  }

  @Override
  public void deleteMenu(Integer id) {
    log.info("Deleting portal menu with id: {}", id);
    menuRepository.deleteById(id);
  }
}
