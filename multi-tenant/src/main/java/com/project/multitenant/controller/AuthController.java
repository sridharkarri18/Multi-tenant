package com.project.multitenant.controller;

import com.project.multitenant.dto.AuthResponse;
import com.project.multitenant.dto.LoginRequest;
import com.project.multitenant.dto.SignupRequest;
import com.project.multitenant.services.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for logging in and signing up Org Admins / End Users")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login to get JWT", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/signup/org-admin")
    @Operation(summary = "Register an organization admin", description = "Creates a new organization and an associated admin user")
    public ResponseEntity<String> registerOrgAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.signupOrgAdmin(signUpRequest);
        return ResponseEntity.ok("Organization Admin registered successfully!");
    }

    @PostMapping("/signup/user")
    @Operation(summary = "Register an end user", description = "Creates a new end user linked to an organization")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest request) {
        authService.signupEndUser(request);
        return ResponseEntity.ok("End User registered successfully!");
    }
}
