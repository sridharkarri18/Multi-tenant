package com.project.multitenant.dto;

public class FlagCheckResponse {
    private String featureKey;
    private boolean enabled;

    public FlagCheckResponse() {}

    public FlagCheckResponse(String featureKey, boolean enabled) {
        this.featureKey = featureKey;
        this.enabled = enabled;
    }

    public String getFeatureKey() { return featureKey; }
    public void setFeatureKey(String featureKey) { this.featureKey = featureKey; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
