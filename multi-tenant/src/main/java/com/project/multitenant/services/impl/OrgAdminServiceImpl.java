package com.project.multitenant.services.impl;

import com.project.multitenant.dto.FeatureFlagRequest;
import com.project.multitenant.dto.FeatureFlagResponse;
import com.project.multitenant.entities.FeatureFlag;
import com.project.multitenant.entities.Organization;
import com.project.multitenant.exceptions.ResourceNotFoundException;
import com.project.multitenant.mapper.FeatureFlagMapper;
import com.project.multitenant.repositories.FeatureFlagRepository;
import com.project.multitenant.repositories.OrganizationRepository;
import com.project.multitenant.services.OrgAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrgAdminServiceImpl implements OrgAdminService {

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public FeatureFlagResponse createFlag(Long organizationId, FeatureFlagRequest request) {
        Organization org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        if (featureFlagRepository.findByFeatureKeyAndOrganizationId(request.getFeatureKey(), organizationId).isPresent()) {
            throw new IllegalArgumentException("Feature flag key already exists in this organization.");
        }

        FeatureFlag flag = new FeatureFlag();
        flag.setFeatureKey(request.getFeatureKey());
        flag.setEnabled(request.getEnabled());
        flag.setOrganization(org);

        FeatureFlag saved = featureFlagRepository.save(flag);
        return FeatureFlagMapper.toResponse(saved);
    }

    @Override
    public FeatureFlagResponse updateFlag(Long organizationId, Long flagId, FeatureFlagRequest request) {
        FeatureFlag flag = featureFlagRepository.findByIdAndOrganizationId(flagId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Feature Flag not found or does not belong to your organization"));

        flag.setEnabled(request.getEnabled());
        // Optional: allow updating feature key as well if needed

        FeatureFlag saved = featureFlagRepository.save(flag);
        return FeatureFlagMapper.toResponse(saved);
    }

    @Override
    public void deleteFlag(Long organizationId, Long flagId) {
        FeatureFlag flag = featureFlagRepository.findByIdAndOrganizationId(flagId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Feature Flag not found or does not belong to your organization"));
        featureFlagRepository.delete(flag);
    }

    @Override
    public List<FeatureFlagResponse> getFlags(Long organizationId) {
        return featureFlagRepository.findByOrganizationId(organizationId).stream()
                .map(FeatureFlagMapper::toResponse)
                .collect(Collectors.toList());
    }
}
