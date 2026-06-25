package com.project.multitenant.repositories;

import com.project.multitenant.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
