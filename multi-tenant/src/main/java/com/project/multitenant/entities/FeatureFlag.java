package com.project.multitenant.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "feature_flags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"feature_key", "organization_id"})
})
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_key", nullable = false)
    private String featureKey;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public FeatureFlag() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFeatureKey() { return featureKey; }
    public void setFeatureKey(String featureKey) { this.featureKey = featureKey; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
