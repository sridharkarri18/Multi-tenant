package com.project.multitenant.services;

import com.project.multitenant.dto.OrganizationRequest;
import com.project.multitenant.dto.OrganizationResponse;
import java.util.List;

public interface SuperAdminService {
    OrganizationResponse createOrganization(OrganizationRequest request);
    List<OrganizationResponse> getAllOrganizations();
    List<com.project.multitenant.dto.UserResponse> getUsersByOrganization(Long orgId);
}
