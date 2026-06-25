package com.project.multitenant.mapper;

import com.project.multitenant.dto.FeatureFlagResponse;
import com.project.multitenant.entities.FeatureFlag;

public class FeatureFlagMapper {
    public static FeatureFlagResponse toResponse(FeatureFlag flag) {
        if (flag == null) return null;
        return new FeatureFlagResponse(
                flag.getId(),
                flag.getFeatureKey(),
                flag.isEnabled(),
                flag.getOrganization().getId()
        );
    }
}
