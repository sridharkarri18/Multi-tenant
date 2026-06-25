package com.project.multitenant.controller;

import com.project.multitenant.dto.FlagCheckResponse;
import com.project.multitenant.security.UserPrincipal;
import com.project.multitenant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user/flags")
@Tag(name = "End User", description = "Endpoints for end users to check feature flags")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check")
    public ResponseEntity<FlagCheckResponse> checkFlag(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam String featureKey) {
        
        Long orgId = currentUser.getOrganizationId();
        return ResponseEntity.ok(userService.checkFlag(orgId, featureKey));
    }
}
