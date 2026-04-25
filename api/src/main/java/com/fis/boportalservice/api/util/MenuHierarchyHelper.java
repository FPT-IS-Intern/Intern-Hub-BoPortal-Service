package com.fis.boportalservice.api.util;

import com.fis.boportalservice.api.dto.response.PortalMenuResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuHierarchyHelper {

  private MenuHierarchyHelper() {
    // Private constructor to prevent instantiation
  }

  public static List<PortalMenuResponse> buildMenuHierarchy(List<PortalMenuResponse> flatMenus) {
    if (flatMenus == null || flatMenus.isEmpty()) {
      return Collections.emptyList();
    }

    Map<Integer, PortalMenuResponse> menuMap = flatMenus.stream()
        .collect(Collectors.toMap(PortalMenuResponse::getId, m -> m));

    List<PortalMenuResponse> rootMenus = new ArrayList<>();

    for (PortalMenuResponse menu : flatMenus) {
      if (menu.getParentId() == null) {
        rootMenus.add(menu);
      } else if (menuMap.containsKey(menu.getParentId())) {
        PortalMenuResponse parent = menuMap.get(menu.getParentId());
        if (parent.getChildren() == null) {
          parent.setChildren(new ArrayList<>());
        }
        parent.getChildren().add(menu);
      } else {
        // Keep orphan items visible (e.g. parent filtered by role) so URL-level access still works.
        rootMenus.add(menu);
      }
    }

    return rootMenus;
  }
}
