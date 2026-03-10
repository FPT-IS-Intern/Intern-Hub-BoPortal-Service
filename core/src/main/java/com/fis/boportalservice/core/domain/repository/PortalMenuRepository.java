package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.PortalMenu;

import java.util.List;
import java.util.Optional;

public interface PortalMenuRepository {
  List<PortalMenu> findActiveMenus();

  List<PortalMenu> findAll();

  Optional<PortalMenu> findById(Integer id);

  PortalMenu save(PortalMenu menu);

  void deleteById(Integer id);
}
