package com.fis.boportalservice.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "portal_menu_role",
    uniqueConstraints = @UniqueConstraint(name = "uk_portal_menu_role", columnNames = {"menu_id", "role_code"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortalMenuRoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "menu_id", nullable = false)
  private Integer menuId;

  @Column(name = "role_code", nullable = false, length = 100)
  private String roleCode;
}

