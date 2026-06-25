package com.project.multitenant.controller;

import com.project.multitenant.dto.FeatureFlagRequest;
import com.project.multitenant.dto.FeatureFlagResponse;
import com.project.multitenant.security.UserPrincipal;
import com.project.multitenant.services.OrgAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/orgadmin/flags")
@Tag(name = "Org Admin", description = "Endpoints for Org Admins to manage feature flags")
public class OrgAdminController {

    @Autowired
    private OrgAdminService orgAdminService;

    @PostMapping
    public ResponseEntity<FeatureFlagResponse> createFlag(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody FeatureFlagRequest request) {
        return ResponseEntity.ok(orgAdminService.createFlag(currentUser.getOrganizationId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlagResponse> updateFlag(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long id,
            @Valid @RequestBody FeatureFlagRequest request) {
        return ResponseEntity.ok(orgAdminService.updateFlag(currentUser.getOrganizationId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlag(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long id) {
        orgAdminService.deleteFlag(currentUser.getOrganizationId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FeatureFlagResponse>> getFlags(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(orgAdminService.getFlags(currentUser.getOrganizationId()));
    }
}
