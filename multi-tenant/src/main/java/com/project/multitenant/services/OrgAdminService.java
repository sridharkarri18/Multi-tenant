package com.project.multitenant.services;

import com.project.multitenant.dto.FeatureFlagRequest;
import com.project.multitenant.dto.FeatureFlagResponse;
import java.util.List;

public interface OrgAdminService {
    FeatureFlagResponse createFlag(Long organizationId, FeatureFlagRequest request);
    FeatureFlagResponse updateFlag(Long organizationId, Long flagId, FeatureFlagRequest request);
    void deleteFlag(Long organizationId, String featureKey);
    List<FeatureFlagResponse> getFlags(Long organizationId);
}
