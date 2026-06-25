package com.project.multitenant.mapper;

import com.project.multitenant.dto.OrganizationResponse;
import com.project.multitenant.entities.Organization;

public class OrganizationMapper {
    public static OrganizationResponse toResponse(Organization organization) {
        if (organization == null) return null;
        return new OrganizationResponse(
                organization.getId(),
                organization.getName()
        );
    }
}
