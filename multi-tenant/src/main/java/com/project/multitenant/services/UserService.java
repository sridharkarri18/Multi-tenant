package com.project.multitenant.services;

import com.project.multitenant.dto.FlagCheckResponse;

public interface UserService {
    FlagCheckResponse checkFlag(Long organizationId, String featureKey);
}
