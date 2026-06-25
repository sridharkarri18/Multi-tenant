package com.project.multitenant.services.impl;

import com.project.multitenant.dto.OrganizationRequest;
import com.project.multitenant.dto.OrganizationResponse;
import com.project.multitenant.entities.Organization;
import com.project.multitenant.mapper.OrganizationMapper;
import com.project.multitenant.repositories.OrganizationRepository;
import com.project.multitenant.services.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponse createOrganization(OrganizationRequest request) {
        Organization org = new Organization();
        org.setName(request.getName());
        Organization saved = organizationRepository.save(org);
        return OrganizationMapper.toResponse(saved);
    }

    @Override
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(OrganizationMapper::toResponse)
                .collect(Collectors.toList());
    }
}
