package com.project.multitenant.services.impl;

import com.project.multitenant.dto.FlagCheckResponse;
import com.project.multitenant.entities.FeatureFlag;
import com.project.multitenant.repositories.FeatureFlagRepository;
import com.project.multitenant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Override
    public FlagCheckResponse checkFlag(Long organizationId, String featureKey) {
        Optional<FeatureFlag> flagOpt = featureFlagRepository.findByFeatureKeyAndOrganizationId(featureKey, organizationId);
        
        if (flagOpt.isPresent()) {
            return new FlagCheckResponse(featureKey, flagOpt.get().isEnabled());
        }
        
        // Default to false if not found
        return new FlagCheckResponse(featureKey, false);
    }
}
