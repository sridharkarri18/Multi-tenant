package com.project.multitenant.dto;

public class FeatureFlagResponse {
    private Long id;
    private String featureKey;
    private boolean enabled;
    private Long organizationId;

    public FeatureFlagResponse() {}

    public FeatureFlagResponse(Long id, String featureKey, boolean enabled, Long organizationId) {
        this.id = id;
        this.featureKey = featureKey;
        this.enabled = enabled;
        this.organizationId = organizationId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFeatureKey() { return featureKey; }
    public void setFeatureKey(String featureKey) { this.featureKey = featureKey; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
}
