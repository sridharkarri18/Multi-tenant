package com.project.multitenant.controller;

import com.project.multitenant.dto.OrgUserRequest;
import com.project.multitenant.dto.SignupRequest;
import com.project.multitenant.security.UserPrincipal;
import com.project.multitenant.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orgadmin/users")
@Tag(name = "Org Admin - Users", description = "Endpoints for Org Admins to provision users for their organization")
public class OrgAdminUserController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<String> createEndUser(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody OrgUserRequest request) {
        
        // Map OrgUserRequest to SignupRequest internally, using the OrgAdmin's organizationId
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(request.getEmail());
        signupRequest.setPassword(request.getPassword());
        signupRequest.setOrganizationId(currentUser.getOrganizationId());
        
        authService.signupEndUser(signupRequest);
        return ResponseEntity.ok("End User registered successfully for your organization!");
    }
}
