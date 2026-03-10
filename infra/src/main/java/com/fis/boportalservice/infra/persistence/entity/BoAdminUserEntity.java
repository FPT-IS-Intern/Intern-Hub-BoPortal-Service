package com.fis.boportalservice.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bo_admin_user", schema = "ih_bo_portal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoAdminUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true, length = 100)
  private String username;

  @Column(name = "password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Column(name = "display_name", length = 255)
  private String displayName;

  @Column(nullable = false, length = 20)
  private String status;

  @Column(name = "failed_attempt")
  private Integer failedAttempt;

  @Column(name = "locked_until")
  private LocalDateTime lockedUntil;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @Column(name = "role_codes", columnDefinition = "TEXT")
  private String roleCodes;

  @Column(name = "permission_codes", columnDefinition = "TEXT")
  private String permissionCodes;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}

