package com.project.multitenant.controller;

import com.project.multitenant.dto.OrganizationRequest;
import com.project.multitenant.dto.OrganizationResponse;
import com.project.multitenant.services.SuperAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/superadmin/organizations")
@Tag(name = "Super Admin", description = "Endpoints for Super Admin to onboard organizations")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(@Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(superAdminService.createOrganization(request));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
        return ResponseEntity.ok(superAdminService.getAllOrganizations());
    }

    @GetMapping("/{orgId}/users")
    public ResponseEntity<List<com.project.multitenant.dto.UserResponse>> getUsersByOrg(@PathVariable Long orgId) {
        return ResponseEntity.ok(superAdminService.getUsersByOrganization(orgId));
    }
}
