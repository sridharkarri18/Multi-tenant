package com.project.multitenant.repositories;

import com.project.multitenant.entities.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    Optional<FeatureFlag> findByFeatureKeyAndOrganizationId(String featureKey, Long organizationId);
    Optional<FeatureFlag> findByIdAndOrganizationId(Long id, Long organizationId);
    List<FeatureFlag> findByOrganizationId(Long organizationId);
}
