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
      if (menu.getParentId() == null || !menuMap.containsKey(menu.getParentId())) {
        rootMenus.add(menu);
      } else {
        PortalMenuResponse parent = menuMap.get(menu.getParentId());
        if (parent.getChildren() == null) {
          parent.setChildren(new ArrayList<>());
        }
        parent.getChildren().add(menu);
      }
    }

    return rootMenus;
  }
}
