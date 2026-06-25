package com.project.multitenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeatureFlagRequest {
    @NotBlank(message = "Feature key is required")
    private String featureKey;

    @NotNull(message = "Enabled status is required")
    private Boolean enabled;

    public FeatureFlagRequest() {}

    public String getFeatureKey() { return featureKey; }
    public void setFeatureKey(String featureKey) { this.featureKey = featureKey; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
