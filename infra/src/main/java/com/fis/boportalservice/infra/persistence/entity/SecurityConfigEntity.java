package com.fis.boportalservice.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "security_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityConfigEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "min_password_length", nullable = false)
  private Integer minPasswordLength;

  @Column(name = "max_password_length", nullable = false)
  private Integer maxPasswordLength;

  @Column(name = "min_uppercase_chars", nullable = false)
  private Integer minUppercaseChars;

  @Column(name = "min_special_chars", nullable = false)
  private Integer minSpecialChars;

  @Column(name = "min_numeric_chars", nullable = false)
  private Integer minNumericChars;

  @Column(name = "password_expiry_days", nullable = false)
  private Integer passwordExpiryDays;

  @Column(name = "allow_whitespace", nullable = false)
  private Boolean allowWhitespace;

  @Column(name = "auto_logout_minutes", nullable = false)
  private Integer autoLogoutMinutes;

  @Column(name = "max_login_attempts", nullable = false)
  private Integer maxLoginAttempts;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column(name = "updated_by")
  private UUID updatedBy;
}

