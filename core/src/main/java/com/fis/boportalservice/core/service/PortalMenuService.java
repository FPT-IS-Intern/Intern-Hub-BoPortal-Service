package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.PortalMenu;

import java.util.List;

public interface PortalMenuService {
  List<PortalMenu> getAvailableMenus(List<String> userRoles);

  List<PortalMenu> getAllMenus();

  PortalMenu getMenuById(Integer id);

  PortalMenu createMenu(PortalMenu menu);

  PortalMenu updateMenu(Integer id, PortalMenu menu);

  void deleteMenu(Integer id);
}
