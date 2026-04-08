package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.core.domain.repository.PortalMenuRepository;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.service.PortalMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PortalMenuServiceImpl implements PortalMenuService {

  private final PortalMenuRepository menuRepository;

  @Override
  public List<PortalMenu> getAvailableMenus(List<String> userRoles) {
    List<PortalMenu> activeMenus = menuRepository.findActiveMenusByRoles(userRoles);
    if (activeMenus.isEmpty()) {
      return activeMenus;
    }

    Map<Integer, PortalMenu> menuById = activeMenus.stream()
        .collect(Collectors.toMap(PortalMenu::getId, menu -> menu, (left, right) -> left, HashMap::new));
    Set<Integer> visibleIds = new HashSet<>(menuById.keySet());

    return activeMenus.stream()
        .filter(menu -> hasVisibleParentChain(menu, menuById, visibleIds))
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
          return new ClientSideException(ErrorCode.NOT_FOUND, "Portal menu not found");
        });
  }

  @Override
  public PortalMenu createMenu(PortalMenu menu) {
    log.info("event=PORTAL_MENU_PERSIST_CREATE code={}", menu.getCode());
    menu.setRoleCodes(normalizeRoleCodes(menu.getRoleCodes()));
    return menuRepository.save(menu);
  }

  @Override
  public PortalMenu updateMenu(Integer id, PortalMenu menu) {
    log.info("event=PORTAL_MENU_PERSIST_UPDATE id={}", id);
    PortalMenu existing = getMenuById(id);

    existing.setCode(menu.getCode());
    existing.setTitle(menu.getTitle());
    existing.setPath(menu.getPath());
    existing.setIcon(menu.getIcon());
    existing.setParentId(menu.getParentId());
    existing.setRoleCodes(normalizeRoleCodes(menu.getRoleCodes()));
    existing.setSortOrder(menu.getSortOrder());
    existing.setStatus(menu.getStatus());

    return menuRepository.save(existing);
  }

  @Override
  public void deleteMenu(Integer id) {
    log.info("event=PORTAL_MENU_PERSIST_DELETE id={}", id);
    menuRepository.deleteById(id);
  }

  private boolean hasVisibleParentChain(PortalMenu menu, Map<Integer, PortalMenu> menuById, Set<Integer> visibleIds) {
    Integer parentId = menu.getParentId();
    if (parentId == null) {
      return true;
    }
    if (!visibleIds.contains(parentId)) {
      return false;
    }
    PortalMenu parent = menuById.get(parentId);
    return parent == null || hasVisibleParentChain(parent, menuById, visibleIds);
  }

  private List<String> normalizeRoleCodes(List<String> roleCodes) {
    if (roleCodes == null || roleCodes.isEmpty()) {
      return List.of();
    }
    LinkedHashSet<String> unique = new LinkedHashSet<>();
    for (String code : roleCodes) {
      if (code == null) {
        continue;
      }
      String trimmed = code.trim();
      if (!trimmed.isEmpty()) {
        unique.add(trimmed);
      }
    }
    return List.copyOf(unique);
  }

}

