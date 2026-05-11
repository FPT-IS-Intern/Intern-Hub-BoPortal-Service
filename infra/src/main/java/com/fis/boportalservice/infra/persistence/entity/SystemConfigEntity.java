package com.fis.boportalservice.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfigEntity {

  @Id
  @Column(name = "id")
  private Integer id;

  @Column(name = "app_name", nullable = false, length = 255)
  private String appName;

  @Column(name = "logo_url", columnDefinition = "TEXT")
  private String logoUrl;

  @Column(name = "default_language", nullable = false, length = 10)
  private String defaultLanguage;

  @Column(name = "work_start_time", nullable = false)
  private LocalTime workStartTime;

  @Column(name = "work_end_time", nullable = false)
  private LocalTime workEndTime;

  @Column(name = "auto_checkout_time", nullable = false)
  private LocalTime autoCheckoutTime;

  @Column(name = "attendance_offset_minutes")
  private Integer attendanceOffsetMinutes;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column(name = "updated_by")
  private UUID updatedBy;
}

