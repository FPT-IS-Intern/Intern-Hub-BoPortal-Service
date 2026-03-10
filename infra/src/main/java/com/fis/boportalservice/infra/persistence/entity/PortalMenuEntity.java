package com.fis.boportalservice.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "portal_menu", schema = "ih_bo_portal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortalMenuEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true, length = 50)
  private String code;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(length = 255)
  private String path;

  @Column(length = 100)
  private String icon;

  @Column(name = "parent_id")
  private Integer parentId;

  @Column(name = "permission_code", length = 100)
  private String permissionCode;

  @Column(name = "sort_order")
  @Builder.Default
  private Integer sortOrder = 0;

  @Column(length = 20)
  @Builder.Default
  private String status = "ACTIVE";

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column(name = "created_by")
  private UUID createdBy;

  @Column(name = "updated_by")
  private UUID updatedBy;
}
