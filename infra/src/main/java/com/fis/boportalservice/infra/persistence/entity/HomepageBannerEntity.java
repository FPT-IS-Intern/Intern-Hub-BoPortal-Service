package com.fis.boportalservice.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "homepage_banners", schema = "bo_portal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomepageBannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "desktop_image_url", nullable = false, columnDefinition = "TEXT")
    private String desktopImageUrl;

    @Column(name = "mobile_image_url", columnDefinition = "TEXT")
    private String mobileImageUrl;

    @Column(name = "image_alt_text", length = 255)
    private String imageAltText;

    @Column(name = "action_type", nullable = false, length = 20)
    @Builder.Default
    private String actionType = "NONE";

    @Column(name = "action_target", length = 500)
    private String actionTarget;

    @Column(name = "open_in_new_tab")
    @Builder.Default
    private Boolean openInNewTab = true;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

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
